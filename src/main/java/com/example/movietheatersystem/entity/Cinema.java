package com.example.movietheatersystem.entity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cinemas")
public class Cinema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private String description;
    private String contact;
    @Column(name = "poster_url")
    private String posterUrl; // 影院海报图 URL


    @OneToMany(mappedBy = "cinema", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ScreeningRoom> screeningRooms = new ArrayList<>();

    // Constructors
    public Cinema() {}

    public Cinema(String name, String address, String description, String contact) {
        this.name = name;
        this.address = address;
        this.description = description;
        this.contact = contact;
    }

    // Getters and Setters
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public List<ScreeningRoom> getScreeningRooms() {
        return screeningRooms;
    }

    public void setScreeningRooms(List<ScreeningRoom> screeningRooms) {
        this.screeningRooms = screeningRooms;
    }

    public String getPosterUrl() {
        return posterUrl;
    }
    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

}