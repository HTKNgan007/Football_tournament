package vn.ngaha.footballTournament.models;

import jakarta.persistence.*;

@Entity
public class MatchResults {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "match_id")
    private Matches match;

    @Column(name = "team1_score")
    private int team1Score;
    @Column(name = "team2_score")
    private int team2Score;

    private String note;

    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Matches getMatch() {
		return match;
	}

	public void setMatch(Matches match) {
		this.match = match;
	}

	public int getTeam1Score() {
		return team1Score;
	}

	public void setTeam1Score(int team1Score) {
		this.team1Score = team1Score;
	}

	public int getTeam2Score() {
		return team2Score;
	}

	public void setTeam2Score(int team2Score) {
		this.team2Score = team2Score;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
    
}
