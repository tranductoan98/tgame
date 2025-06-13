package com.example.dto;

import java.time.LocalDateTime;

import com.example.entity.Items;
import com.example.entity.PlayerInventory;
import com.example.enums.EquipType;

public class InventoryItemDTO {

    private Integer itemId;
    private String name;
    private int coin;
    private short gold;
    private short type;
    private short icon;
    private byte sell;
    private byte expiredDay;
    private byte zorder;
    private byte gender;
    private byte level;
    private String animation;

    private boolean visible;
    private boolean equipped;
    private EquipType equip_type;
    private LocalDateTime dateExpired;

    public InventoryItemDTO(PlayerInventory pi) {
        Items item = pi.getItems();
        this.itemId = item.getItemId();
        this.name = item.getName();
        this.coin = item.getCoin();
        this.gold = item.getGold();
        this.type = item.getType();
        this.icon = item.getIcon();
        this.sell = item.getSell();
        this.expiredDay = item.getExpiredDay();
        this.zorder = item.getZorder();
        this.gender = item.getGender();
        this.level = item.getLevel();
        this.animation = item.getAnimation();
        this.visible = pi.isVisible();
        this.equipped = pi.isEquipped();
        this.equip_type = item.getEquip_type();
        this.dateExpired = pi.getDateExpired();
    }

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCoin() {
		return coin;
	}

	public void setCoin(int coin) {
		this.coin = coin;
	}

	public short getGold() {
		return gold;
	}

	public void setGold(short gold) {
		this.gold = gold;
	}

	public short getType() {
		return type;
	}

	public void setType(short type) {
		this.type = type;
	}

	public short getIcon() {
		return icon;
	}

	public void setIcon(short icon) {
		this.icon = icon;
	}

	public byte getSell() {
		return sell;
	}

	public void setSell(byte sell) {
		this.sell = sell;
	}

	public byte getExpiredDay() {
		return expiredDay;
	}

	public void setExpiredDay(byte expiredDay) {
		this.expiredDay = expiredDay;
	}

	public byte getZorder() {
		return zorder;
	}

	public void setZorder(byte zorder) {
		this.zorder = zorder;
	}

	public byte getGender() {
		return gender;
	}

	public void setGender(byte gender) {
		this.gender = gender;
	}

	public byte getLevel() {
		return level;
	}

	public void setLevel(byte level) {
		this.level = level;
	}

	public String getAnimation() {
		return animation;
	}

	public void setAnimation(String animation) {
		this.animation = animation;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isEquipped() {
		return equipped;
	}

	public void setEquipped(boolean equipped) {
		this.equipped = equipped;
	}

	public LocalDateTime getDateExpired() {
		return dateExpired;
	}

	public void setDateExpired(LocalDateTime dateExpired) {
		this.dateExpired = dateExpired;
	}

	public EquipType getEquip_type() {
		return equip_type;
	}

	public void setEquip_type(EquipType equip_type) {
		this.equip_type = equip_type;
	}

}

