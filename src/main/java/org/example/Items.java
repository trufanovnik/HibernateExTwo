package org.example;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "items")
@NoArgsConstructor
public class Items {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public long id;

    @Column(name = "val")
    public int val;

    @Version
    @Column(name = "version")
    public int version;

    Items(int val){
        this.val = val;
    }
}
