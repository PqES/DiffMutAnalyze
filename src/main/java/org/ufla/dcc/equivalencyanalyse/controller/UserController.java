package org.ufla.dcc.equivalencyanalyse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.ufla.dcc.equivalencyanalyse.model.AlertType;
import org.ufla.dcc.equivalencyanalyse.model.Login;
import org.ufla.dcc.equivalencyanalyse.model.Message;
import org.ufla.dcc.equivalencyanalyse.model.entity.User;
import org.ufla.dcc.equivalencyanalyse.service.UserService;
import org.ufla.dcc.equivalencyanalyse.util.Utils;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

  @Autowired
  private UserService userService;

  @GetMapping("/home")
  public String getHome(Model theModel, @ModelAttribute("message") Message message,
      HttpSession session) {
    if (session.getAttribute("user") == null) {
      return "redirect:/user/login";
    }
    return "user/user-home";
  }

  @GetMapping("/login")
  public String getLogin(Model theModel, @ModelAttribute("login") Login login,
      @ModelAttribute("message") Message message) {
    return "user/user-login";
  }

  @GetMapping("/logout")
  public String getLogout(HttpSession session) {
    session.removeAttribute("user");

    return "redirect:/user/login";
  }

  @GetMapping("/register")
  public String getRegister(Model theModel, @ModelAttribute("user") User user,
      @ModelAttribute("message") Message message) {
    return "user/user-register";
  }

  @PostMapping("/login")
  public String postLogin(@ModelAttribute("login") Login login, RedirectAttributes attributes,
      HttpSession session) {
    try {
      User user = userService.validateUser(login);
      session.setAttribute("user", user);
      attributes.addFlashAttribute("message",
          new Message("Bem vindo " + user.getName() + "!", AlertType.SUCCESS));

      return "redirect:/user/home";
    } catch (Exception e) {
      e.printStackTrace();
      attributes.addFlashAttribute("login", login);
      attributes.addFlashAttribute("message",
          new Message(Utils.getMessagesRecursive(e), AlertType.DANGER));
      return "redirect:/user/login";
    }
  }

  @PostMapping("/register")
  public String postRegister(@ModelAttribute("user") User user, RedirectAttributes attributes,
      HttpSession session) {
    try {
      user = userService.register(user);
      if (user == null) {
        throw new Exception("Erro ao registrar usuário!");
      }
      session.setAttribute("user", user);
      attributes.addFlashAttribute("message",
          new Message("Bem vindo " + user.getName() + "!", AlertType.SUCCESS));
      System.out.println("register SUCCESS");
      return "redirect:/user/home";
    } catch (Exception e) {
      e.printStackTrace();
      attributes.addFlashAttribute("message",
          new Message(Utils.getMessagesRecursive(e), AlertType.DANGER));
      if (user != null) {
        attributes.addFlashAttribute("user", user);
      }
      return "redirect:/user/register";
    }
  }

}
