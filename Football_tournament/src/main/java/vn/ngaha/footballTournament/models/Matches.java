package vn.ngaha.footballTournament.models;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
public class Matches {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tournament_id")
    private Tournaments tournament;

    @ManyToOne
    @JoinColumn(name = "team1_id")
    private Teams team1;

    @ManyToOne
    @JoinColumn(name = "team2_id")
    private Teams team2;

    private LocalDate matchDate;
    private String location;

    @OneToOne(mappedBy = "match", cascade = CascadeType.ALL)
    private MatchResults result;

    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Tournaments getTournament() {
		return tournament;
	}

	public void setTournament(Tournaments tournament) {
		this.tournament = tournament;
	}

	public Teams getTeam1() {
		return team1;
	}

	public void setTeam1(Teams team1) {
		this.team1 = team1;
	}

	public Teams getTeam2() {
		return team2;
	}

	public void setTeam2(Teams team2) {
		this.team2 = team2;
	}

	public LocalDate getMatchDate() {
		return matchDate;
	}

	public void setMatchDate(LocalDate matchDate) {
		this.matchDate = matchDate;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public MatchResults getResult() {
		return result;
	}

	public void setResult(MatchResults result) {
		this.result = result;
	}
}
