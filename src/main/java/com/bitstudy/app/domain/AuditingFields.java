package com.bitstudy.app.domain;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import java.time.LocalDateTime;

public class AuditingFields {
    //메타데이터
    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt; // 생성일자

    @CreatedBy
    @Column(nullable = false,length = 100)
    private String createdBy; // 생성자

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifiedAt; // 수정일자

    @LastModifiedBy
    @Column(nullable = false,length = 100)
    private String modifiedBy; // 수정자
}
