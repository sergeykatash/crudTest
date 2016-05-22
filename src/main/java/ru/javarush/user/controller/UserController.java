package ru.javarush.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.javarush.user.model.User;
import ru.javarush.user.service.UserService;

import java.util.List;

@Controller
public class UserController {
   private UserService userService;

    @Autowired(required = true)
    @Qualifier(value = "userService")
    public void setUserService(UserService userService) {
        this.userService = userService;
    }



    @RequestMapping(value="/users", method = RequestMethod.GET)
    public ModelAndView listOfUsers(@RequestParam(required = false) Integer page) {
        ModelAndView modelAndView = new ModelAndView("users");

        modelAndView.addObject("user", new User());
        List<User> users = userService.listUsers();
        PagedListHolder<User> pagedListHolder = new PagedListHolder<User>(users);
        pagedListHolder.setPageSize(5);
        modelAndView.addObject("maxPages", pagedListHolder.getPageCount());

        if(page==null || page < 1 || page > pagedListHolder.getPageCount())page=1;

        modelAndView.addObject("page", page);
        if(page == null || page < 1 || page > pagedListHolder.getPageCount()){
            pagedListHolder.setPage(0);
            modelAndView.addObject("users", pagedListHolder.getPageList());
        }
        else if(page <= pagedListHolder.getPageCount()) {
            pagedListHolder.setPage(page-1);
            modelAndView.addObject("users", pagedListHolder.getPageList());
        }
        return modelAndView;
    }


    @RequestMapping(value = "/users/add", method = RequestMethod.POST)
    public String addUser(@ModelAttribute("user") User user)
    {
        if (user.getId() == 0)
        {
            this.userService.addUser(user);
        }else {
            this.userService.updateUser(user);
        }
        return "redirect:/users";
    }

    @RequestMapping("/remove/{id}")
    public String deleteUser(@PathVariable("id") int id)
    {
        this.userService.deleteUser(id);
        return "redirect:/users";
    }

    @RequestMapping("edit/{id}")
     public String editUser(@PathVariable("id") int id, Model model) {
        model.addAttribute("user", this.userService.getUserById(id));
        model.addAttribute("listUsers", this.userService.listUsers());

        return "users";
    }


    @RequestMapping(value ="/userdata",method = RequestMethod.GET)
    public String userData(int id, Model model) {
        model.addAttribute("user", this.userService.getUserById(id));

        return "userdata";
    }





}
