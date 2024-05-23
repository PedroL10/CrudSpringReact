package com.crud.demo.service;

import com.crud.demo.exception.StudentAlreadyExistException;
import com.crud.demo.exception.StudentNotFoundException;
import com.crud.demo.model.Student;
import com.crud.demo.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService implements  IStudentService{
    private StudentRepository studentRepository;

    @Override
    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Student addStudent(Student student) {

        if(studentAlreadyExists(student.getEmail())) {
            throw new StudentAlreadyExistException(student.getEmail() + " already exists");
        }
        return studentRepository.save(student);
    }

    @Override
    public Student updateStudent(Student student, Long id) {
        return studentRepository.findById(id).map(st -> {
            st.setFirstName(student.getFirstName());
            st.setLastName(student.getLastName());
            st.setEmail(student.getEmail());
            st.setDepartment(student.getDepartment());
            return studentRepository.save(st);
        }).orElseThrow(() -> new StudentNotFoundException("Sorry, student not be found"));
    }

    @Override
    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException("Sorry, " +
                "student not found"));
    }

    @Override
    public void deleteStudent(Long id) {
    if(!studentRepository.existsById(id)) {
            throw new StudentNotFoundException("Sorry, student not found");
        }
    }

    private boolean studentAlreadyExists(String email) {
        return studentRepository.findByEmail(email).isPresent();
    }


}
