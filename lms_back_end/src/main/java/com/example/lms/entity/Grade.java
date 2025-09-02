package com.example.lms.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "grades", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"enrollment_id"}) // එක් enrollment එකකට එක් grade එකක් පමණයි
})
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id", nullable = false)
    private Enrollment enrollment;

    @Column(nullable = false)
    private String gradeValue; // e.g., "A", "B+", "C"

    @Column(nullable = false)
    private Double score;      // e.g., 85.5, 72.0

    private String remarks;
}