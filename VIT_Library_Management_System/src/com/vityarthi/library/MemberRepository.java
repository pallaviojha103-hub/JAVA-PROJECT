package com.vityarthi.library;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for Member entities.
 * Supports StudentMember and FacultyMember polymorphic persistence using PreparedStatements.
 */
public class MemberRepository {
    private final DatabaseManager dbManager;

    public MemberRepository() {
        this.dbManager = DatabaseManager.getInstance();
    }

    public MemberRepository(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public boolean addMember(Member member) {
        String sql = "INSERT INTO members (member_id, name, email, type, max_books, fine_rate) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, member.getMemberId());
            pstmt.setString(2, member.getName());
            pstmt.setString(3, member.getEmail());
            pstmt.setString(4, member.getMemberType());
            pstmt.setInt(5, member.getMaxBooks());
            pstmt.setDouble(6, member.getFineRatePerDay());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[MemberRepository] Error adding member: " + e.getMessage());
        }
        return false;
    }

    public Member findById(String memberId) {
        String sql = "SELECT * FROM members WHERE member_id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, memberId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMember(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[MemberRepository] Error finding member by ID: " + e.getMessage());
        }
        return null;
    }

    public List<Member> findAll() {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM members ORDER BY name ASC";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                members.add(mapResultSetToMember(rs));
            }
        } catch (SQLException e) {
            System.err.println("[MemberRepository] Error retrieving all members: " + e.getMessage());
        }
        return members;
    }

    public boolean deleteMember(String memberId) {
        String sql = "DELETE FROM members WHERE member_id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, memberId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[MemberRepository] Error deleting member: " + e.getMessage());
        }
        return false;
    }

    private Member mapResultSetToMember(ResultSet rs) throws SQLException {
        String memberId = rs.getString("member_id");
        String name = rs.getString("name");
        String email = rs.getString("email");
        String type = rs.getString("type");

        if ("FACULTY".equalsIgnoreCase(type)) {
            return new FacultyMember(memberId, name, email);
        } else {
            return new StudentMember(memberId, name, email);
        }
    }
}
