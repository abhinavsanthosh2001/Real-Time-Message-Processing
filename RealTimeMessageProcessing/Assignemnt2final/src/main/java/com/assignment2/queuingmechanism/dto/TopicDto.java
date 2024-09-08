package com.assignment2.queuingmechanism.dto;

import com.assignment2.queuingmechanism.constants.Constants;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TopicDto {
    long id;
    @NotBlank(message = Constants.NAME_BLANK_MESSAGE)
    String name;
    @Size(min = 2, max = 100, message = Constants.DESCRIPTION_SIZE_MESSAGE)
    String description;
    List<ConsumerDto> subscribers;
}
