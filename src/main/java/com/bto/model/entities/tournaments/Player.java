package com.bto.model.entities.tournaments;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Entity
public class Player extends AbstractEntity {
	
	private String firstName;
	
	private String lastName;
	
	private String gender;
	
	private Date birthday;
	
	private String address;
	
	private String postCode;
	
	private String city;
	
	private String email;
	
	private String phonenumber;
	
	//niveau de jeu
	private String level;
	
	@ManyToOne(fetch=FetchType.EAGER, cascade = CascadeType.DETACH)
	private Club club;
	
	@OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "firstPlayer")
	private Set<DoubleTeam> player1OfADoubleteam = new HashSet<>();
	
	@OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "secondPlayer")
	private Set<DoubleTeam> player2OfADoubleteam = new HashSet<>();

	@Transient
	private List<DoubleTeam> doubleTeams;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((club == null) ? 0 : club.hashCode());
		result = prime * result
				+ ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((gender == null) ? 0 : gender.hashCode());
		result = prime * result
				+ ((lastName == null) ? 0 : lastName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (club == null) {
			if (other.club != null)
				return false;
		} else if (!club.equals(other.club))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (gender == null) {
			if (other.gender != null)
				return false;
		} else if (!gender.equals(other.gender))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		return true;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	@Transient
	public List<DoubleTeam> getDoubleTeams() {
		if(doubleTeams == null){
			doubleTeams = new ArrayList<DoubleTeam>(player1OfADoubleteam);
			doubleTeams.addAll(player2OfADoubleteam);
		}
		return doubleTeams;
	}

	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public Club getClub() {
		return club;
	}

	public void setClub(Club club) {
		this.club = club;
	}

	@Override
	public String toString() {
		return "Player [id ="+id+", firstName=" + firstName + ", lastName=" + lastName
				+ ", club=" + club + ", doubleteams=" + teamToString() + "]";
	}

	private String teamToString() {
		StringBuilder toString = new StringBuilder();
		for(DoubleTeam doubleTeam : getPlayer1OfADoubleteam()){
			toString.append(doubleTeam.toString());
			toString.append(", ");
		}
		for(DoubleTeam doubleTeam : getPlayer2OfADoubleteam()){
			toString.append(doubleTeam.toString());
			toString.append(", ");
		}
		if(toString.length() > 0) {
			toString.delete(toString.length() - 2, toString.length() - 1);
		}
		return toString.toString();
	}

	public void setPlayer1OfADoubleteam(Set<DoubleTeam> player1OfADoubleteam) {
		this.player1OfADoubleteam = player1OfADoubleteam;
	}

	public void setPlayer2OfADoubleteam(Set<DoubleTeam> player2OfADoubleteam) {
		this.player2OfADoubleteam = player2OfADoubleteam;
	}

	public Set<DoubleTeam> getPlayer1OfADoubleteam() {
		return player1OfADoubleteam;
	}

	public Set<DoubleTeam> getPlayer2OfADoubleteam() {
		return player2OfADoubleteam;
	}

}
