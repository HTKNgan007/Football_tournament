package vn.ngaha.footballTournament.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import vn.ngaha.footballTournament.models.MatchResults;
import vn.ngaha.footballTournament.models.Matches;
import vn.ngaha.footballTournament.repositories.MatchRepository;
import vn.ngaha.footballTournament.services.impl.MatchResultsServiceImpl;

@Controller
public class MatchResultsController {

	@Autowired
    private MatchRepository matchesRepository;

    @Autowired
    private MatchResultsServiceImpl matchResultsService;

    @GetMapping("/matches/{id}/result")
    public String showResultForm(@PathVariable Long id, Model model) {
        Matches match = matchesRepository.findById(id).orElseThrow();
        MatchResults result = matchResultsService.getOrCreateResultByMatch(match);

        model.addAttribute("match", match);
        model.addAttribute("result", result);
        return "result-form";
    }

    @PostMapping("/matches/{id}/result")
    public String submitResult(@PathVariable Long id, @ModelAttribute MatchResults result) {
        Matches match = matchesRepository.findById(id).orElseThrow();
        result.setMatch(match);
        matchResultsService.saveResult(result);
        return "redirect:/tournaments/" + match.getTournament().getId() + "/matches";
    }
}
