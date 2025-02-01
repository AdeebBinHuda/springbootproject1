package com.firstProject.springbootproject1.service;

import com.firstProject.springbootproject1.entity.Student;
import com.firstProject.springbootproject1.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class StudentService {
    // class object
    @Autowired
    private StudentRepository studentRepository;

    @Value("src/main/resources/static/images")
    private String uploadDir;


    // create list which list student data
    public List<Student> getAllStudents(){

        return studentRepository.findAll();
    }

    public void saveStudent(Student s, MultipartFile imageFile) throws IOException {
        if(imageFile != null && !imageFile.isEmpty()){
            String imageFileName =saveImage(imageFile, s);
            s.setImage(imageFileName);
        }
        studentRepository.save(s);

    }

     public Student getStudentById(int id){
         return studentRepository.findById(id)
                 .orElseThrow(()-> new EntityNotFoundException(" Student with id " + id + " not found"));
     }

     public void deleteStudent(int id){
       if (!studentRepository.existsById(id)){
           throw new EntityNotFoundException("Student not found with id "+id);
       }

        studentRepository.deleteById(id);
     }

     public Student updateStudent(int id,Student updateStudent){

         Student existingStudent=studentRepository.findById(id)
                 .orElseThrow(()-> new EntityNotFoundException(" Student with id " + id + " not found"));

         if(updateStudent.getName()!=null){

           existingStudent.setName(updateStudent.getName());
         }

         if(updateStudent.getEmail()!=null){

             existingStudent.setEmail(updateStudent.getEmail());
         }

         if(updateStudent.getCallNo()!=null){

             existingStudent.setCallNo(updateStudent.getCallNo());
         }

        return studentRepository.save(existingStudent);
     }

     // if getById is used then this can be workable;  overloading being used
     public void updateStudent(Student updateStudent){

        studentRepository.save(updateStudent);
     }


     // if wants to find by email   this is an exmaple
     public Student findStudentByEmail(String email){
       return studentRepository.findByEmail(email)
               .orElseThrow(()-> new EntityNotFoundException(" Student with email" + email + " not found"));
     }

     private  String saveImage(MultipartFile file, Student s) throws IOException {
         Path uploadPath = Paths.get(uploadDir+"/Students");
         if(!Files.exists(uploadPath)){
             Files.createDirectories(uploadPath);
         }

         // adeeb  =    adehdheitrkds
         String fileName = s.getName()+"_"+ UUID.randomUUID().toString();
       // name the file with proper style and store into filePath
         Path filePath= uploadPath.resolve(fileName);

         Files.copy(file.getInputStream(), filePath);

         return fileName;
     }

}
