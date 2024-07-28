package one.digitalinnovation.desafio.service.impl;

import jakarta.servlet.http.HttpServletResponse;
import one.digitalinnovation.desafio.model.User;
import one.digitalinnovation.desafio.repository.UserRepository;
import one.digitalinnovation.desafio.service.UserService;
import one.digitalinnovation.desafio.service.exception.NotFoundException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findById(Long id) {
        LOGGER.info("User found.");
        return userRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public User create(User userToCreate) {
        if (userRepository.existsByAccountNumber(userToCreate.getAccount().getNumber())) {
            throw new IllegalArgumentException("This account number already exists.");
        }
        LOGGER.info("Creating user with id: {}", userToCreate.getId());
        return userRepository.save(userToCreate);
    }

    @Override
    public User update(User userToUpdate) {
        if (!userRepository.existsById(userToUpdate.getId())) {
            throw new NotFoundException();
        }
        LOGGER.info("Updating user with id: {}", userToUpdate.getId());
        return userRepository.save(userToUpdate);
    }

    @Override
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException();
        }
        userRepository.deleteById(id);
        LOGGER.info("User deleted.");
    }

    @Override
    public void exportToExcel(HttpServletResponse response) throws IOException {
        List<User> users = userRepository.findAll();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Users");

        // Cria o cabeçalho
        Row headerRow = sheet.createRow(0);
        Cell headerCell1 = headerRow.createCell(0);
        headerCell1.setCellValue("ID");
        Cell headerCell2 = headerRow.createCell(1);
        headerCell2.setCellValue("Name");
        Cell headerCell3 = headerRow.createCell(2);
        headerCell3.setCellValue("Account Number");
        Cell headerCell4 = headerRow.createCell(3);
        headerCell4.setCellValue("Agency");
        Cell headerCell5 = headerRow.createCell(4);
        headerCell5.setCellValue("Card Number");

        // Adiciona os dados
        int rowNum = 1;
        for (User user : users) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(user.getId());
            row.createCell(1).setCellValue(user.getName());

            if (user.getAccount() != null) {
                row.createCell(2).setCellValue(user.getAccount().getNumber());
                row.createCell(3).setCellValue(user.getAccount().getAgency());
            } else {
                row.createCell(2).setCellValue("N/A");
                row.createCell(3).setCellValue("N/A");
            }

            if (user.getCard() != null) {
                row.createCell(4).setCellValue(user.getCard().getNumber());
            } else {
                row.createCell(4).setCellValue("N/A");
            }
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=users.xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();
    }

    @Override
    public void exportUserByIdToExcel(Long id, HttpServletResponse response) throws IOException {
        User user = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("User not found"));

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("User");

        // Cria o cabeçalho
        Row headerRow = sheet.createRow(0);
        Cell headerCell1 = headerRow.createCell(0);
        headerCell1.setCellValue("ID");
        Cell headerCell2 = headerRow.createCell(1);
        headerCell2.setCellValue("Name");
        Cell headerCell3 = headerRow.createCell(2);
        headerCell3.setCellValue("Account Number");
        Cell headerCell4 = headerRow.createCell(3);
        headerCell4.setCellValue("Agency");
        Cell headerCell5 = headerRow.createCell(4);
        headerCell5.setCellValue("Card Number");

        // Adiciona os dados
        Row row = sheet.createRow(1);
        row.createCell(0).setCellValue(user.getId());
        row.createCell(1).setCellValue(user.getName());

        if (user.getAccount() != null) {
            row.createCell(2).setCellValue(user.getAccount().getNumber());
            row.createCell(3).setCellValue(user.getAccount().getAgency());
        } else {
            row.createCell(2).setCellValue("N/A");
            row.createCell(3).setCellValue("N/A");
        }

        if (user.getCard() != null) {
            row.createCell(4).setCellValue(user.getCard().getNumber());
        } else {
            row.createCell(4).setCellValue("N/A");
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=user_" + id + ".xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
