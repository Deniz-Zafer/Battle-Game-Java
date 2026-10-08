package proje.model.character;

import proje.model.combat.Attackable;
import proje.model.combat.SkillUser;
import proje.model.combat.ElementType;

public abstract class Character implements Attackable, SkillUser {

    private String name;
    private int health;
    private int maxHealth;
    private int attackPower;
    private int defense;

    //  1 tur için geçici defans bonusu
    private int tempDefenseBonus = 0;

    private ElementType elementType;

    public Character(String name, int maxHealth, int attackPower, int defense) {
        this(name, maxHealth, attackPower, defense, ElementType.NEUTRAL);
    }

    public Character(String name, int maxHealth, int attackPower, int defense, ElementType elementType) {
        this.name = name;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.attackPower = attackPower;
        this.defense = defense;
        this.elementType = elementType;
    }

    // ✅ 1 tur savunma bonusu ekle
    public void addTempDefenseBonus(int bonus) {
        if (bonus < 0) bonus = 0;
        tempDefenseBonus += bonus;
    }

    // ✅ Tur bitince temizle
    public void clearTempDefenseBonus() {
        tempDefenseBonus = 0;
    }

    // ✅ Gerçek defans = base + temp
    public int getTotalDefense() {
        return defense + tempDefenseBonus;
    }

    public void takeDamage(int amount) {
        // ✅ artık total defense kullanıyoruz
        int damage = amount - getTotalDefense();
        if (damage < 0) {
            damage = 0;
        }
        this.health -= damage;
        if (this.health < 0) {
            this.health = 0;
        }
        System.out.println(name + " " + damage + " hasar aldı. Kalan can: " + health);
    }

    public void heal(int amount) {
        this.health += amount;
        if (this.health > maxHealth) {
            this.health = maxHealth;
        }
        System.out.println(name + " " + amount + " can yeniledi. Güncel can: " + health);
    }

    public boolean isAlive() {
        return health > 0;
    }

    public void printStatus() {
        System.out.println("[" + name + "] HP: " + health + "/" + maxHealth +
                " | ATK: " + attackPower + " | DEF: " + defense + " (Total: " + getTotalDefense() + ")");
    }

    // ----- Getter/Setter -----

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        if (health < 0) {
            this.health = 0;
        } else if (health > maxHealth) {
            this.health = maxHealth;
        } else {
            this.health = health;
        }
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        if (maxHealth <= 0) {
            maxHealth = 1;
        }
        this.maxHealth = maxHealth;
        if (health > maxHealth) {
            health = maxHealth;
        }
    }

    public int getAttackPower() {
        return attackPower;
    }

    public void setAttackPower(int attackPower) {
        if (attackPower < 0) attackPower = 0;
        this.attackPower = attackPower;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        if (defense < 0) defense = 0;
        this.defense = defense;
    }

    public ElementType getElementType() {
        return elementType;
    }

    public void setElementType(ElementType elementType) {
        this.elementType = elementType;
    }
}
