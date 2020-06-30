package validators;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.excilys.model.ComputerDTO;
import com.excilys.service.ComputerDTOProblems;

public class ComputerDTOValidator implements Validator {

    @Override
    public boolean supports(final Class<?> clazz) {
        return ComputerDTO.class.equals(clazz);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        ComputerDTO targetDTO = (ComputerDTO) target;
        if (targetDTO.getName() == null || targetDTO.getName().isEmpty()) {
            errors.rejectValue("name", ComputerDTOProblems.INVALID_NAME.getExplanation());
        }

    }

}
