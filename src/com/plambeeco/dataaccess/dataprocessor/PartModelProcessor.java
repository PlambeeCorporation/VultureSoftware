package com.plambeeco.dataaccess.dataprocessor;

import com.plambeeco.dataaccess.repository.IPartModelRepository;
import com.plambeeco.dataaccess.repository.PartModelRepository;
import com.plambeeco.models.IPartModel;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class PartModelProcessor {
    private PartModelProcessor() {
        throw new RuntimeException("This class should not be initialized!");
    }
    /**
     * Validates part model and asks repository to insert the part model into the database.
     * @param partModel New Part Model.
     */
    public static void add(final IPartModel partModel) {
        if(validatePartModel(partModel)){
            IPartModelRepository partModelRepository = new PartModelRepository();
            partModelRepository.add(partModel);
        }
    }

    public static void addAll(int jobId, final Collection<IPartModel> parts){
        if(jobId > 0){
            for (IPartModel part : parts){
                if(!validatePartModel(part)){
                    return;
                }
            }

            IPartModelRepository partModelRepository = new PartModelRepository();
            partModelRepository.addAll(parts);
            partModelRepository.addPartsNeeded(jobId, parts);
        }
    }

    public static void addPartName(String partName){
        if(validatePartName(partName)){
            IPartModelRepository partModelRepository = new PartModelRepository();
            partModelRepository.addPartName(partName);
        }
    }

    public static void update(final IPartModel partModel) {
        if(validatePartModel(partModel)){
            IPartModelRepository partModelRepository = new PartModelRepository();
            partModelRepository.update(partModel);
        }
    }

    public static void updatePartName(String oldPartName, String newPartName){
        if(validatePartName(oldPartName) && validatePartName(newPartName)){
            IPartModelRepository partModelRepository = new PartModelRepository();
            partModelRepository.updatePartName(oldPartName, newPartName);
        }
    }

    public static void remove(final IPartModel partModel) {
        if(validatePartModel(partModel)){
            IPartModelRepository partModelRepository = new PartModelRepository();
            partModelRepository.remove(partModel);
        }
    }

    public IPartModel getById(final int id){
        if(id >= 0){
            IPartModelRepository partModelRepository = new PartModelRepository();
            IPartModel partModel = partModelRepository.getById(id);

            if(validatePartModel(partModel)){
                return partModel;
            }
        }

        return null;
    }

    public static List<IPartModel> getAll() {
        IPartModelRepository partModelRepository = new PartModelRepository();
        List<IPartModel> parts = partModelRepository.getAll();

        for(IPartModel part : parts){
            if(!validateLoadPartModel(part)){
                System.out.println("Invalid part was loaded, when loading all parts.");
                parts.remove(part);
            }
        }

        return parts;
    }

    public static Set<String> getAllPartNames() {
        IPartModelRepository partModelRepository = new PartModelRepository();
        Set<String> partNames = partModelRepository.getAllPartNames();

        for (String partName: partNames) {
            if(!validatePartName(partName)){
                partNames.remove(partName);
            }
        }

        return partNames;
    }

    public static List<IPartModel> getJobPartsNeeded(int jobId){
        IPartModelRepository partModelRepository = new PartModelRepository();
        List<IPartModel> parts = partModelRepository.getJobPartsNeeded(jobId);

        for(IPartModel part : parts){
            if(!validateLoadPartModel(part)){
                System.out.println("Invalid part was loaded, when loading all parts.");
                parts.remove(part);
            }
        }

        return parts;
    }

    private static boolean validatePartModel(final IPartModel partModel){
        boolean isValid = true;

        if(partModel.getPartName().isEmpty()){
            isValid = false;
            System.out.println("Error: Part Name cannot be null!");
        }
        if(partModel.getPartQuantity() <= 0){
            isValid = false;
            System.out.println("Error: The Part Quantity must be higher than 0");
        }

        return isValid;
    }

    private static boolean validateLoadPartModel(final IPartModel partModel){
        boolean isValid = validatePartModel(partModel);

        if(partModel.getPartId() <= 0){
            isValid = false;
            System.out.println("Error: Invalid Part ID");
        }

        return isValid;
    }

    private static boolean validatePartName(String partName){
        boolean isValid = true;

        if(partName.isEmpty()){
            isValid = false;
            System.out.println("Error: Part Name cannot be null!");
        }

        return isValid;
    }
}
