package com.example.partyhallfinder.serviceImpl;

import com.example.partyhallfinder.Components.Credentials;
import com.example.partyhallfinder.Components.SignupDetails;
import com.example.partyhallfinder.Exception.InvalidCredentialsException;
import com.example.partyhallfinder.Exception.NotFoundException;
import com.example.partyhallfinder.Models.Admin;
import com.example.partyhallfinder.Models.Admin;
import com.example.partyhallfinder.Models.Owner;
import com.example.partyhallfinder.Repositories.AdminRepository;
import com.example.partyhallfinder.Services.AdminService;
import com.example.partyhallfinder.payload.AdminDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;
    private final ModelMapper mapper;
    @Override
    public AdminDto addAdmin(SignupDetails admin) throws Exception {
        String tempEmailId = admin.getEmail();
        if(tempEmailId != null && !tempEmailId.isEmpty()) {
            Optional<Admin> adminObj = adminRepository.findByEmail(tempEmailId);
            if(adminObj.isPresent()) {
                throw new Exception("User with "+tempEmailId+" is already exist");
            }
            else{
                Admin newAdmin = new Admin();
                newAdmin.setImg(admin.getImg());
                newAdmin.setEmail(admin.getEmail());
                newAdmin.setDob(admin.getDob());
                newAdmin.setPassword(admin.getPassword());
                newAdmin.setPhone(admin.getPhone());
                newAdmin.setLastName(admin.getLastName());
                newAdmin.setFirstName(admin.getFirstName());
//                newAdmin.setPartyHallIds(new ArrayList<String>());

                adminRepository.save(newAdmin);
            }
        }
        return mapper.map(adminRepository.findByEmail(tempEmailId).get(), AdminDto.class);
    }

    @Override
    public AdminDto signIn(Credentials credentials) throws NotFoundException, InvalidCredentialsException {
        String tempPassword = credentials.getPassword();
        String tempEmail = credentials.getEmail();
        Admin adminObj=null;
        if(tempEmail!=null && tempPassword!=null) {
            adminObj = adminRepository.findOwnerByEmail(tempEmail);
            if(adminObj==null) {
                throw new NotFoundException("doesn't Exists");
            }
            else if(!adminObj.getPassword().equals(tempPassword)){
                throw new InvalidCredentialsException("Invalid password");
            }
        }
        return mapper.map(adminObj, AdminDto.class);
    }

    @Override
    public AdminDto getAdminById(String id) {
        Admin admin = adminRepository.findById(id).get();
        return mapper.map(admin, AdminDto.class);

    }
}
