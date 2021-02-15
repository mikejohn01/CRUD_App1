package ru.mikejohn.springcource.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.mikejohn.springcource.dao.PersonDAO;
import ru.mikejohn.springcource.models.Person;

import javax.validation.Valid;

/**
 * Created by mikejohn on 01.12.2020.
 */
@Controller
@RequestMapping("/people")
public class PeopleController {
    //внедряем объект DAO. Спринг автоматически внедрит сюда бин PersonDAO
    private final PersonDAO personDAO;

    @Autowired
    public PeopleController(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    //возвращает список людей
    @GetMapping ()  // тут пусто, т.к. уже есть @RequestMapping и при /people будем попадать в этот метод
    public String index(Model model) {
        //получим всех людей из DAO и передадим на отображение в Thymeleaf.
        model.addAttribute("people", personDAO.index());
        return "people/index";
    }

    //возвращает одного человека по его id
    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        //получим одного человека по id из DAO и передадим на отображение в представление
        model.addAttribute("person", personDAO.show(id));
        return "people/show";
    }

    //метод будет возвращать форму для создания нового человека
//    @GetMapping("/new")
//    public String newPerson (Model model){  //тут создается модель и в неё кладется пустой объект person
//        model.addAttribute("person", new Person());//внедрим модель в html форму
//        return "people/new";
//    }
// равнозначный метод, но с аннотацией @ModelAttribute в аргументе метода
// как и выше создастся модель с пустым объектом класса Person
    @GetMapping("/new")
    public String newPerson (@ModelAttribute("person") Person person) {
        return "people/new";
    }

    //метод будет принимать на вход post-запрос, брать из этого запроса данные и добавлять нового человека в БД
    @PostMapping()
    public String create (@ModelAttribute("person") @Valid Person person,
                          BindingResult bindingResult){
        if (bindingResult.hasErrors())
            return "people/new";    //если есть ошибки валидации, то возвращаем форму создания объекта
        personDAO.save(person);
        return "redirect:/people";
    }

    //метод возвращающий представление для редактировния человека
    @GetMapping ("{id}/edit")
    public String edit (Model model, @PathVariable ("id") int id) { //извлечем id из запроса
        // в форме редактирования будут данные человека. Для этого добавим в модель аттрибут - человека:
        model.addAttribute("person", personDAO.show(id));
        // теперь у нас в представлении есть модель
        return "people/edit";
    }

    //метод для редактировния человека
    @PatchMapping("/{id}")
    public String update (@ModelAttribute("person") @Valid Person person, BindingResult bindingResult,
                          @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "{id}/edit";
        //return "people/edit

        personDAO.update(id, person);
        return "redirect:/people";
    }

    //метод удаления человека
    @DeleteMapping ("/{id}")
    public String delete(@PathVariable("id") int id){
        personDAO.delete(id);
        return"redirect:/people";
    }
}

