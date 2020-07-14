package com.excilys.adapters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import com.excilys.mapper.DateMapper;
import com.excilys.model.ComputerDto;

@Component
public class ComputerDtoValidator {
    Logger LOG = org.slf4j.LoggerFactory.getLogger(ComputerDtoValidator.class);

    public void validate(@Nullable final ComputerDto computerDTO) throws InvalidComputerDtoException {
        if (computerDTO == null) {
            throw new InvalidComputerDtoException(Arrays.asList(ComputerDTOProblems.NULL_DTO));
        }
        List<ComputerDTOProblems> problems = new ArrayList<>();
        validateDto(computerDTO, problems);
        if (!problems.isEmpty()) {
            throw new InvalidComputerDtoException(problems);
        }
    }

    public void validateDto(@NonNull final ComputerDto computerDTO, @NonNull final List<ComputerDTOProblems> problems) {

        if (computerDTO.getName() == null || computerDTO.getName().trim().isEmpty()) {
            problems.add(ComputerDTOProblems.INVALID_NAME);
        }

        if (computerDTO.getIntroduced() != null && !computerDTO.getIntroduced().isEmpty()) {
            if (!DateMapper.stringToLocalDate(computerDTO.getIntroduced()).isPresent()) {
                problems.add(ComputerDTOProblems.INVALID_DATE_INTRO);
            }
        }
        if (computerDTO.getDiscontinued() != null && !computerDTO.getDiscontinued().trim().isEmpty()
                && !DateMapper.stringToLocalDate(computerDTO.getDiscontinued()).isPresent()) {
            this.LOG.error(computerDTO.getDiscontinued());
            problems.add(ComputerDTOProblems.INVALID_DATE_DISCO);
        }

        if (computerDTO.getId() < 0) {
            problems.add(ComputerDTOProblems.INVALID_ID);
        }

        return;
    }
}
