package dao;

import entity.Department;
import util.JdbcV1;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAO {

    // JDBC V1
    private String stmSELECT =
            "SELECT [Id], [Name], [Description] FROM [dbo].[Departments]";

    // JDBC V2
    private String stmSELECT_byId = """
            SELECT [Id], [Name], [Description]
            FROM [dbo].[Departments]
            WHERE [Id] = ?
            """;

    private String stmSELECT_byName = """
    SELECT [Id], [Name], [Description]
    FROM [dbo].[Departments]
    WHERE [Name] = ?
    """;

    // JDBC V3
    private String callSELECT = "{CALL spSelectAll()}";
    private String callSELECT_byId = "{CALL spSelectById(?)}";
    private String callINSERT = "{CALL spInsert(?,?,?)}";
    private String callUPDATE = "{CALL spUpdate(?,?,?)}";
    private String callDELETE_byId = "{CALL spDeleteById(?)}";

    // =========================================================
    // CHECK CONNECTION
    // =========================================================
    public void checkDepartmentDAO() {

        try (Connection con = JdbcV1.getConnection()) {

            if (con != null) {
                System.out.println("Connect Success!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================================================
    // JDBC V1 - FIND ALL
    // =========================================================
    public List<Department> findAllV1() {

        List<Department> list = new ArrayList<>();

        try {

            ResultSet rs = JdbcV1.executeQuery(stmSELECT);

            while (rs.next()) {

                Department d = new Department();

                d.setId(rs.getString("Id"));
                d.setName(rs.getString("Name"));
                d.setDescription(rs.getString("Description"));

                list.add(d);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // =========================================================
    // JDBC V2 - FIND ALL
    // =========================================================
    public List<Department> findAllV2() {

        List<Department> list = new ArrayList<>();

        try (
                Connection con = JdbcV1.getConnection();
                PreparedStatement ps = con.prepareStatement(stmSELECT);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                Department d = new Department();

                d.setId(rs.getString("Id"));
                d.setName(rs.getString("Name"));
                d.setDescription(rs.getString("Description"));

                list.add(d);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // =========================================================
    // JDBC V3 - FIND ALL
    // =========================================================
    public List<Department> findAll() {

        List<Department> list = new ArrayList<>();

        try (
                Connection con = JdbcV1.getConnection();
                CallableStatement cs = con.prepareCall(callSELECT);
                ResultSet rs = cs.executeQuery()
        ) {

            while (rs.next()) {

                Department d = new Department();

                d.setId(rs.getString("Id"));
                d.setName(rs.getString("Name"));
                d.setDescription(rs.getString("Description"));

                list.add(d);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // =========================================================
    // JDBC V3 - FIND BY ID
    // =========================================================
    public Department findById(String id) {

        try (
                Connection con = JdbcV1.getConnection();
                CallableStatement cs = con.prepareCall(callSELECT_byId)
        ) {

            cs.setString(1, id);

            ResultSet rs = cs.executeQuery();

            if (rs.next()) {

                Department d = new Department();

                d.setId(rs.getString("Id"));
                d.setName(rs.getString("Name"));
                d.setDescription(rs.getString("Description"));

                return d;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // =========================================================
    // JDBC V2 - FIND BY NAME
    // =========================================================
    public List<Department> findByName(String keyword) {

        List<Department> list = new ArrayList<>();

        try (
                Connection con = JdbcV1.getConnection();
                PreparedStatement ps = con.prepareStatement(stmSELECT_byName)
        ) {

            ps.setString(1, keyword);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Department d = new Department();

                d.setId(rs.getString("Id"));
                d.setName(rs.getString("Name"));
                d.setDescription(rs.getString("Description"));

                list.add(d);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // =========================================================
    // INSERT
    // =========================================================
    public boolean insert(Department d) {

        try (
                Connection con = JdbcV1.getConnection();
                CallableStatement cs = con.prepareCall(callINSERT)
        ) {

            cs.setString(1, d.getId());
            cs.setString(2, d.getName());
            cs.setString(3, d.getDescription());

            return cs.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // =========================================================
    // UPDATE
    // =========================================================
    public boolean update(Department d) {

        try (
                Connection con = JdbcV1.getConnection();
                CallableStatement cs = con.prepareCall(callUPDATE)
        ) {

            cs.setString(1, d.getId());
            cs.setString(2, d.getName());
            cs.setString(3, d.getDescription());

            return cs.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // =========================================================
    // DELETE BY ID
    // =========================================================
    public boolean deleteById(String id) {

        try (
                Connection con = JdbcV1.getConnection();
                CallableStatement cs = con.prepareCall(callDELETE_byId)
        ) {

            cs.setString(1, id);

            return cs.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
