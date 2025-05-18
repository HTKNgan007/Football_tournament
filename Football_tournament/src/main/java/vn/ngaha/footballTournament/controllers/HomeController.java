package vn.ngaha.footballTournament.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import vn.ngaha.footballTournament.services.TournamentService;

@Controller
public class HomeController {

	@Autowired
    private TournamentService tournamentService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("tournaments", tournamentService.getAllTournaments());
        return "home"; // Tên file giao diện: home.html
    }
}
