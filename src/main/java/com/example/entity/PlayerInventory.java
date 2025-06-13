package com.example.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "player_inventory")
public class PlayerInventory {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "playerid", nullable = true)
    private Player player;

    @ManyToOne
    @JoinColumn(name = "itemId", nullable = true)
    private Items items;

    @Column(name = "isequipped", nullable = false)
    private boolean isEquipped = false;

    @Column(name = "isvisible", nullable = false)
    private boolean isVisible = true;

    @Column(name = "dateexpired", nullable = false)
    private LocalDateTime dateExpired = LocalDateTime.of(2000, 1, 1, 0, 0);

	public PlayerInventory() {
	}
	
	public PlayerInventory(Player player, Items items, boolean isEquipped, boolean isVisible,
			LocalDateTime dateExpired) {
		this.player = player;
		this.items = items;
		this.isEquipped = isEquipped;
		this.isVisible = isVisible;
		this.dateExpired = dateExpired;
	}



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Items getItems() {
		return items;
	}

	public void setItems(Items items) {
		this.items = items;
	}

	public boolean isEquipped() {
		return isEquipped;
	}

	public void setEquipped(boolean isEquipped) {
		this.isEquipped = isEquipped;
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	public LocalDateTime getDateExpired() {
		return dateExpired;
	}

	public void setDateExpired(LocalDateTime dateExpired) {
		this.dateExpired = dateExpired;
	}

}
