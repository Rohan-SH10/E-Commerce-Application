package com.retail.e_com.mailservice;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageModel {
    private String to;
    private String subject;
    private String text;
}
