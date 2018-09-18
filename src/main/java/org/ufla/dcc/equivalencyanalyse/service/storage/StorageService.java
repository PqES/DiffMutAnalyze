package org.ufla.dcc.equivalencyanalyse.service.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

  void deleteAll();

  void initialize() throws IOException;

  String loadContent(String filepath) throws IOException;

  Path load(String filename);

  Stream<Path> loadAll() throws Exception;

  Resource loadAsResource(String filename) throws Exception;

  void store(MultipartFile file, String directory);

}
