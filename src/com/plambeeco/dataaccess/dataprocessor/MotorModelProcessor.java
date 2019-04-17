package com.plambeeco.dataaccess.dataprocessor;

import com.plambeeco.dataaccess.repository.IMotorModelRepository;
import com.plambeeco.dataaccess.repository.MotorModelRepository;
import com.plambeeco.models.IMotorModel;

import java.util.List;

public class MotorModelProcessor {

    private MotorModelProcessor() {
        throw new RuntimeException("This class should not be initialized!");
    }
    /**
     * Validates motor model before asking repository to add the motor model to the database.
     * @param motorModel    New Motor Model.
     */
    public static void add(IMotorModel motorModel) {
        if(validateMotorModel(motorModel)){
            IMotorModelRepository motorModelRepository = new MotorModelRepository();
            motorModelRepository.add(motorModel);
        }
    }

    /**
     * Validates motor model before asking repository to update the record in the database.
     * @param motorModel    Updated Motor model.
     */
    public static void update(IMotorModel motorModel) {
        if(validateMotorModel(motorModel)){
            IMotorModelRepository motorModelRepository = new MotorModelRepository();
            motorModelRepository.update(motorModel);
        }
    }

    /**
     * Validates motor model unique identifier isn't null and asks repository to remove motor model.
     * @param motorModel    Motor Model to remove.
     */
    public static void remove(IMotorModel motorModel) {
        if(motorModel.getMotorId() >= 0){
            IMotorModelRepository motorModelRepository = new MotorModelRepository();
            motorModelRepository.remove(motorModel);
        }
    }

    /**
     * Validates motor model unique identifier isn't null and asks repository to get motor model
     * with the matching personId.
     * After receiving motor model, validates the data is correct and returns motor model.
     * @param id    Motor's Model unique identifier.
     * @return  Motor Model.
     */
    public static IMotorModel getById(int id) {
        if(id >= 0){
            IMotorModelRepository motorModelRepository = new MotorModelRepository();
            IMotorModel motorModel = motorModelRepository.getById(id);

            if(validateMotorModel(motorModel)){
                return motorModel;
            }
        }

        return null;
    }

    /**
     * Asks repository for list of all motor models, then validates all of them.
     * If all are valid, returns all motor models.
     * @return List of all Motor Models.
     */
    public static List<IMotorModel> getAll() {
        IMotorModelRepository motorModelRepository = new MotorModelRepository();
        List<IMotorModel> motors = motorModelRepository.getAll();

        for(IMotorModel motor : motors){
            if(!validateMotorModel(motor)){
                System.out.println("Invalid motor was loaded, when loading all motors");
                return null;
            }
        }

        return motors;
    }

    /**
     * Validates motor model.
     * @param motorModel    Motor Model to validate.
     * @return  true is Motor Model is valid, false otherwise.
     */
    private static boolean validateMotorModel(IMotorModel motorModel){
        boolean isValid = true;

        if(motorModel.getMotorType().isEmpty()){
            System.out.println("Motor type cannot be empty!");
            isValid = false;
        }

        if(motorModel.getManufacturer().isEmpty()){
            System.out.println("Manufacturer cannot be empty!");
            isValid = false;
        }

        int estimatedYearOfManufactureLength = (int)(Math.log10(motorModel.getEstimatedYearOfManufacture())+1);

        if(estimatedYearOfManufactureLength != 4){
            System.out.println("Until 10000 year, the year will have only 4 digits.");
            isValid = false;
        }

        return isValid;
    }
}
