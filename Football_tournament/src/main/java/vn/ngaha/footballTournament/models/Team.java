package vn.ngaha.footballTournament.models;

import java.util.List;

import jakarta.persistence.*;

@Entity
public class Team {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "tournament_id")
    private Tournament tournament;

    @OneToMany(mappedBy = "team1", cascade = CascadeType.ALL)
    private List<Match> matchesAsTeam1;

    @OneToMany(mappedBy = "team2", cascade = CascadeType.ALL)
    private List<Match> matchesAsTeam2;

    @OneToOne(mappedBy = "team", cascade = CascadeType.ALL)
    private Standing standing;

	
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Tournament getTournament() {
		return tournament;
	}

	public void setTournament(Tournament tournament) {
		this.tournament = tournament;
	}

	public List<Match> getMatchesAsTeam1() {
		return matchesAsTeam1;
	}

	public void setMatchesAsTeam1(List<Match> matchesAsTeam1) {
		this.matchesAsTeam1 = matchesAsTeam1;
	}

	public List<Match> getMatchesAsTeam2() {
		return matchesAsTeam2;
	}

	public void setMatchesAsTeam2(List<Match> matchesAsTeam2) {
		this.matchesAsTeam2 = matchesAsTeam2;
	}

	public Standing getStanding() {
		return standing;
	}

	public void setStanding(Standing standing) {
		this.standing = standing;
	}

}
