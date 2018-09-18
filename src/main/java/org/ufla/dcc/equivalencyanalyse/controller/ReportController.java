package org.ufla.dcc.equivalencyanalyse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.ufla.dcc.equivalencyanalyse.dto.report.ProjectReportStatisticsDto;
import org.ufla.dcc.equivalencyanalyse.dto.report.ReportDto;
import org.ufla.dcc.equivalencyanalyse.exception.NotAdminProjectException;
import org.ufla.dcc.equivalencyanalyse.exception.NotAuthorException;
import org.ufla.dcc.equivalencyanalyse.model.AlertType;
import org.ufla.dcc.equivalencyanalyse.model.Message;
import org.ufla.dcc.equivalencyanalyse.model.entity.MutantReport;
import org.ufla.dcc.equivalencyanalyse.model.entity.User;
import org.ufla.dcc.equivalencyanalyse.service.ReportService;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/report")
public class ReportController {

  @Autowired
  private ReportService reportService;

  @PostMapping("")
  @ResponseBody
  public ResponseEntity<String> postReport(@RequestBody ReportDto report, HttpSession session) {
    User user = (User) session.getAttribute("user");
    if (user == null) {
      return new ResponseEntity<String>("", HttpStatus.UNAUTHORIZED);
    }
    try {
      MutantReport mutantReport = reportService.save(report, user);
      return new ResponseEntity<String>(mutantReport.getId().toString(), HttpStatus.OK);
    } catch (NotAuthorException e) {
      e.printStackTrace();
      return new ResponseEntity<String>("", HttpStatus.UNAUTHORIZED);
    } catch (Exception e) {
      e.printStackTrace();
      return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/{projectHash}")
  public String getAnalyzeProject(Model model, RedirectAttributes attributes,
      @PathVariable String projectHash, HttpSession session) {
    User user = (User) session.getAttribute("user");
    if (user == null) {
      attributes.addFlashAttribute("message",
          new Message("Usuário não está para acessar relatórios de projetos!", AlertType.DANGER));
      return "redirect:/user/home";
    }
    try {
      ProjectReportStatisticsDto projectReportStatistics =
          reportService.getProjectReportStatistics(projectHash, user);
      model.addAttribute("projectReportStatistics", projectReportStatistics);
      return "report/report-project";
    } catch (NotAdminProjectException e) {
      attributes.addFlashAttribute("message",
          new Message("Usuário não está para acessar relatórios de projetos!", AlertType.DANGER));
      return "redirect:/user/home";
    }
  }

}
