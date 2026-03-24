package com.mco.entity;

import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Account {

    @Id
    @GeneratedValue
    private Long id;                          // 1

    private String accountNumber;             // 2 (Unique business key)

    private String accountHolderName;         // 3

    private String accountType;               // 4 (Savings, Current)

    private double balance;                   // 5

    private String currency;                  // 6 (INR, USD)

    private String status;                    // 7 (ACTIVE, BLOCKED)

    private LocalDateTime createdAt;          // 8

    private LocalDateTime updatedAt;          // 9

    private LocalDateTime lastTransactionAt;  // 10

    private double minimumBalance;            // 11

    private double interestRate;              // 12

    private String branchName;                // 13

    private String ifscCode;                  // 14

    private String nomineeName;               // 15

    private String nomineeRelation;           // 16

    private boolean jointAccount;             // 17

    private String panNumber;                 // 18

    private String aadhaarNumber;             // 19

    private String remarks;                   // 20

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Account account = (Account) o;
        return getId() != null && Objects.equals(getId(), account.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
