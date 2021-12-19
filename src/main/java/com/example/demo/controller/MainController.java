package com.example.demo.controller;

import au.com.bytecode.opencsv.CSVReader;
import com.example.demo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.sql.*;
import java.util.*;

@Controller
public class MainController {
    private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521/orcl321";

    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    private UserRepo userRepo;

    @GetMapping
    public String PasController(Model model) {
        return "fin";
    }

    @GetMapping("No")
    public String StrController(@RequestParam(name = "name", defaultValue = "World") String name, Model model) {
        model.addAttribute("name", name);
        return "No";
    }

    @PostMapping("No")
    public String add(@RequestParam String text,@RequestParam("file") MultipartFile file,Model model) throws IOException, SQLException {
        int count = 0;
        boolean flag = false;
        File uploadDir = new File(uploadPath);

        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        String resultFileName = uploadDir.getAbsolutePath() + "/" + file.getOriginalFilename();
        File fil = new File(resultFileName);
        fil.createNewFile();
        file.transferTo(new File(resultFileName));
        try (Connection connection = DriverManager.getConnection(DB_URL, "test1", "321")) {
            try (CSVReader reader = new CSVReader(new FileReader(resultFileName), ';', '\'')) {
                if (!file.isEmpty() && text.equals("TT$FLAT_OFFERING_INT")) {
                    try (CallableStatement ins = connection.prepareCall("{call insertflat(?, ?,?, ?,?, ?,?, ?,?,?)}")) {
                        String[] record;
                        while ((record = reader.readNext()) != null) {
                            if (count == 0) {
                                if (!record[0].equals("Offering Id")) {
                                    flag = true;
                                    break;
                                } else {
                                    count++;
                                    continue;
                                }
                            }
                            ins.setString(1, record[0]);
                            ins.setString(2, record[1]);
                            ins.setString(3, record[2]);
                            ins.setString(4, record[3]);
                            ins.setString(5, record[4]);
                            ins.setString(6, record[5]);
                            ins.setString(7, record[6]);
                            ins.setString(8, record[7]);
                            ins.setString(9, record[8]);
                            ins.setString(10, record[9]);
                            ins.executeUpdate();
                        }
                        if (flag == false) {
                            CallableStatement cal0 = connection.prepareCall("{call FLAT_OFF}");
                            cal0.executeQuery();
                            CallableStatement cal1 = connection.prepareCall("{call VALIDATION_FLAT_OFF_TEMPLATE}");
                            cal1.executeQuery();
                            CallableStatement cal2 = connection.prepareCall("{call VALIDATION_FLAT_ELGIBILITY}");
                            cal2.executeQuery();
                            CallableStatement cal3 = connection.prepareCall("{call FLAT_OFF_TEMPLATE}");
                            cal3.executeQuery();
                            CallableStatement cal4 = connection.prepareCall("{call flat_eligibility}");
                            cal4.executeQuery();
                            CallableStatement cal5 = connection.prepareCall("{call MERGE_FLAT}");
                            cal5.executeQuery();
                        } else {
                            return "500";
                        }
                    }
                }
                if(!file.isEmpty() && text.equals("TT$RELATION_OVERRIDE_INT")){
                    try (CallableStatement ins = connection.prepareCall("{call insertrel(?, ?,?, ?,?, ?,?, ?,?)}")) {
                        String[] record;
                        while ((record = reader.readNext()) != null) {
                            if (count == 0) {
                                if (!record[0].equals("Relation Id")) {
                                    flag = true;
                                    break;
                                } else {
                                    count++;
                                    continue;
                                }
                            }
                            ins.setString(1, record[0]);
                            ins.setString(2, record[1]);
                            ins.setString(3, record[2]);
                            ins.setString(4, record[3]);
                            ins.setString(5, record[4]);
                            ins.setString(6, record[5]);
                            ins.setString(7, record[6]);
                            ins.setString(8, record[7]);
                            ins.setString(9, record[8]);
                            ins.executeUpdate();
                        }
                        if (flag == false) {
                            CallableStatement cal0 = connection.prepareCall("{call RELATION}");
                            cal0.executeQuery();
                            CallableStatement cal1 = connection.prepareCall("{call VALIDATION_RELATIONS_CHILD}");
                            cal1.executeQuery();
                            CallableStatement cal2 = connection.prepareCall("{call VALIDATION_RELATIONS_CONTEXT}");
                            cal2.executeQuery();
                            CallableStatement cal3 = connection.prepareCall("{call VALIDATION_RELATIONS_PARENT}");
                            cal3.executeQuery();
                            CallableStatement cal4 = connection.prepareCall("{call RELATION_YN}");
                            cal4.executeQuery();
                            CallableStatement cal5 = connection.prepareCall("{call RELATION_CHILD}");
                            cal5.executeQuery();
                            CallableStatement cal6 = connection.prepareCall("{call RELATION_CONTEXT}");
                            cal6.executeQuery();
                            CallableStatement cal7 = connection.prepareCall("{call RELATION_PARENT}");
                            cal7.executeQuery();
                            CallableStatement cal8 = connection.prepareCall("{call MERGE_REL}");
                            cal8.executeQuery();
                    }else {
                        return "500";
                    }
                }
            }
                if(!file.isEmpty() && text.equals("TT$CHAR_OVERRIDE_INT")){
                    try(CallableStatement ins= connection.prepareCall("{call insertchar(?, ?,?, ?,?, ?,?)}")){
                        String[] record;
                        while ((record = reader.readNext()) != null) {
                            if (count == 0) {
                                if (!record[0].equals("Characteristic Override Id")) {
                                    flag = true;
                                    break;
                                } else {
                                    count++;
                                    continue;
                                }
                            }
                            ins.setString(1, record[0]);
                            ins.setString(2, record[1]);
                            ins.setString(3, record[2]);
                            ins.setString(4, record[3]);
                            ins.setString(5, record[4]);
                            ins.setString(6, record[5]);
                            ins.setString(7, record[6]);
                            ins.executeUpdate();
                        }
                    }
                    if (flag == false) {
                        CallableStatement cal0 = connection.prepareCall("{call characteristics}");
                        cal0.executeQuery();
                        CallableStatement cal1 = connection.prepareCall("{call VALIDATION_CHAR_INVOL}");
                        cal1.executeQuery();
                        CallableStatement cal2 = connection.prepareCall("{call VALIDATION_CHAR_FLAT}");
                        cal2.executeQuery();
                        CallableStatement cal3 = connection.prepareCall("{call CHARACTERISTICS_VMM}");
                        cal3.executeQuery();
                        CallableStatement cal4 = connection.prepareCall("{call CHARACTERISTICS_OFFERING}");
                        cal4.executeQuery();
                        CallableStatement cal5 = connection.prepareCall("{call CHARACTERISTICS_CHAR_INVOL}");
                        cal5.executeQuery();
                        CallableStatement cal6 = connection.prepareCall("{call REP_MERGE_CHARACTERISTICS}");
                        cal6.executeQuery();
                    }else {
                        return "500";
                    }
                }

                Statement  stmt = connection.createStatement();
                ResultSet resultSet = stmt.executeQuery("SELECT 'Сессия № '||session_id||' номер строки '||ROWNUM_ID||' '||message FROM TT$MESSAGE WHERE SESSION_ID = (SELECT userenv('sessionid') from dual) and type <> 'info'");
                List<String> a = new ArrayList<>();
                String name = null;
                while (resultSet.next()) {
                    name = resultSet.getString(1);
                    a.add(name);
                }
                model.addAttribute("mes", a);
                ResultSet resultSet1 = stmt.executeQuery("select lpad(' ',1*level,'-')||decode(object_type2,null,parent_name,child_name) as Tree\n" +
                        "from(select ob.name as parent_name,ob2.offering_name as child_name,tre.parent,tre.child,ob2.object_type as object_type2,ob.object_type as object_type1\n" +
                        "from (select r.reference as parent,rr.reference as child\n" +
                        "from tt$references r,tt$references rr\n" +
                        "where r.object_id = rr.object_id \n" +
                        "and r.attr_id = 32\n" +
                        "and rr.attr_id = 33\n" +
                        " union all\n" +
                        "select r.reference as parent,rr.reference as child\n" +
                        "from tt$references r,tt$references rr\n" +
                        "where r.object_id = rr.object_id \n" +
                        "and r.attr_id = 34\n" +
                        "and rr.attr_id = 35\n" +
                        "  union all\n" +
                        "select r.reference as parent,rr.reference as child\n" +
                        "from tt$references r,tt$references rr\n" +
                        "where r.object_id = rr.object_id \n" +
                        "and r.attr_id = 36\n" +
                        "and rr.attr_id = 37\n" +
                        "  union all\n" +
                        "select r.reference as parent,rr.reference as child\n" +
                        "from tt$references r,tt$references rr\n" +
                        "where r.attr_id = 38\n" +
                        "and rr.attr_id = 39 \n" +
                        "union all\n" +
                        "select 0 as parent,r.reference as child\n" +
                        "from tt$references r,tt$references rr\n" +
                        "where r.attr_id = 32\n" +
                        "and rr.attr_id = 33\n" +
                        "and r.object_id = 2\n" +
                        "and rr.object_id = 2) tre,tt$objects ob,\n" +
                        "(select offering_template,offering_name,object_type\n" +
                        "from tt$objects o, tt$flat_offerings fo\n" +
                        "where fo.offering_template = o.objects_id) ob2\n" +
                        "WHERE tre.child = ob.objects_id\n" +
                        "and ob.objects_id = ob2.offering_template(+)\n" +
                        "--and tre.child = ob2.offering_template\n" +
                        "order by parent,child) \n" +
                        "connect by prior child = parent\n" +
                        "start with parent in (0)");
                List<String> a1 = new ArrayList<>();
                String name1 = null;
                Integer level;
                while (resultSet1.next()) {
                    name1 = resultSet1.getString(1);
                    //level = resultSet1.getInt(1);
                    a1.add(name1);
                }
                model.addAttribute("Tree",a1);

        } catch (IOException exc) {
                exc.printStackTrace();
            } catch (SQLException exc) {
                exc.printStackTrace();
            }
            return "No";
        }
    }
}
