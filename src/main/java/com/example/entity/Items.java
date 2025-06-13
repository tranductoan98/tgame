package com.example.entity;

import com.example.enums.EquipType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Items")
public class Items {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer itemId;
	
    @Column(columnDefinition = "TEXT")
    private String name;
    
    //giá bán coin
    private int coin;
    //giá bán gold
    private short gold;
    //loại vật phẩm (0 = trang bị; 1 = tiêu hao; 2 = nhiệm vụ)
    private short type;
    //biểu tượng icon  trong game (img)
    private short icon;
    //bán hay không bán
    private byte sell;
    //số ngày hết hạn (0 = vĩnh viễn; 1 = 1 ngày)
    private byte expiredDay;
    //thứ tự hiển thị khi render
    private byte zorder;
    //giới tính sử dụng (0 = tất cả; 1 = nam; 2 = nữ )
    private byte gender;
    //cấp độ yêu cầu
    private byte level;
    //json config sprite 
    @Column(length = 1000)
    private String animation;
    //item này dùng để làm gì
    @Enumerated(EnumType.STRING)
    @Column(name = "equip_type")
    private EquipType equip_type;

	public Items() {
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

	public EquipType getEquip_type() {
		return equip_type;
	}

	public void setEquip_type(EquipType equip_type) {
		this.equip_type = equip_type;
	}

}
