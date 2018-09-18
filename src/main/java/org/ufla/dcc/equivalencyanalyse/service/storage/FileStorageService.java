package org.ufla.dcc.equivalencyanalyse.service.storage;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class FileStorageService implements StorageService {

  private final Path rootLocation = Paths.get(StorageProperties.getInstance().getUploadDirectory());

  @Autowired
  public FileStorageService() {
    try {
      initialize();
    } catch (IOException e) {
      System.out.printf(
          "IMPORTANT LOG - O diretório %s do serviço de armazenamento não foi criado!",
          rootLocation.getFileName());
    }
  }

  @Override
  public void deleteAll() {
    FileSystemUtils.deleteRecursively(rootLocation.toFile());
  }

  public void initialize() throws IOException {
    Files.createDirectory(rootLocation);
  }

  @Override
  public Path load(String filename) {
    return rootLocation.resolve(filename);
  }

  @Override
  public String loadContent(String filepath) throws IOException {
    return new String(Files.readAllBytes(load(filepath)));
  }

  @Override
  public Stream<Path> loadAll() throws IOException {
    try {
      return Files.walk(this.rootLocation, 1).filter(path -> !path.equals(this.rootLocation))
          .map(this.rootLocation::relativize);
    } catch (IOException e) {
      e.printStackTrace();
      throw new IOException("Failed to read stored files", e);
    }
  }

  @Override
  public Resource loadAsResource(String filename) throws Exception {
    try {
      Path file = load(filename);
      Resource resource = new UrlResource(file.toUri());
      if (resource.exists() || resource.isReadable()) {
        return resource;
      } else {
        throw new Exception(String.format("Arquivo '%s' não existe ou não é legível!", filename));
      }
    } catch (MalformedURLException e) {
      e.printStackTrace();
      throw new Exception("Could not read file: " + filename, e);
    }
  }

  @Override
  public void store(MultipartFile file, String directory) {
    try {
      if (file.isEmpty()) {
        throw new RuntimeException("Failed to store empty file " + file.getOriginalFilename());
      }
      File zip = File.createTempFile(UUID.randomUUID().toString(), "temp");
      FileOutputStream o = new FileOutputStream(zip);
      IOUtils.copy(file.getInputStream(), o);
      o.close();
      String fileNameZip = file.getOriginalFilename();
      int endIndex = fileNameZip.length() - 4;
      File destinationFile =
          new File(StorageProperties.getInstance().getUploadDirectory() + File.separator + directory
              + File.separator + file.getOriginalFilename().substring(0, endIndex));
      try {
        ZipFile zipFile = new ZipFile(zip);
        zipFile.extractAll(destinationFile.getAbsolutePath());
      } catch (ZipException e) {
        e.printStackTrace();
      } finally {
        int cont = 0;
        while (true) {
          if (zip.delete())
            break;
          cont++;
          if (cont == 5)
            break;
        }
        if (cont == 5) {
          System.out.printf("IMPORTANT LOG - O arquivo %s não foi deletado do filesystem!",
              zip.getAbsolutePath());
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException("Failed to store file " + file.getOriginalFilename(), e);
    }
  }

}
