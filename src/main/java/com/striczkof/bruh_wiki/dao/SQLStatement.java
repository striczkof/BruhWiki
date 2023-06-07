package com.striczkof.bruh_wiki.dao;

import java.sql.PreparedStatement;

/**
 * PS constants with SQL statements
 * This should only be used to prepare statements.
 * Constant names: table name_(admin if admin only)_(make/get/set/del)_(specific columns)_number_modifier_(by_value for where except none or only id)
 * @see PS
 * @author Alvin
 */
enum SQLStatement {
    // All SQL statements must be properly named.
    // Constant names: table name_(admin if admin only)_(make/get/set/del)_(specific columns)_number_modifier_(by_value for where except none or only id)
    // User stuff
    USERS_GET_COUNT("SELECT COUNT(id) AS 'count' FROM users"),
    USERS_GET_ONE("SELECT id, username, salt, name, admin, UNIX_TIMESTAMP(created) as 'created', UNIX_TIMESTAMP(last_login) as 'last_login' FROM users WHERE id = ?"),
    USERS_GET_ALL("SELECT id, username, salt, name, admin, UNIX_TIMESTAMP(created) as 'created', UNIX_TIMESTAMP(last_login) as 'last_login' FROM users"),
    USERS_MAKE_ONE("INSERT INTO users (username, hash, salt, name) VALUES (?, ?, ?, ?)"),
    USERS_GET_ID_SALT_ONE_BY_UNAME("SELECT id, salt FROM users WHERE username = ?"),
    USERS_SET_LAST_LOGIN_ONE_BY_ID_HASH("UPDATE users SET last_login = CURRENT_TIMESTAMP WHERE id = ? AND hash = ?"),
    USERS_SET_PASSWORD_ONE_BY_ID_HASH("UPDATE users SET hash = ?, salt = ? WHERE id = ? AND hash = ?"),
    USERS_DEL_ONE_BY_ID_HASH("DELETE FROM users WHERE id = ? AND hash = ?"),
    USERS_GET_ID_UNAME_ONE_LIKE_UNAME("SELECT id, username FROM users WHERE username LIKE %?%"),
    USERS_GET_ID_ONE_BY_UNAME("SELECT id FROM users WHERE username = ?"),
    // User stuff for both user and admin
    USERS_SET_UNAME_ONE("UPDATE users SET username = ? WHERE id = ?"),
    USERS_SET_NAME_ONE("UPDATE users SET name = ? WHERE id = ?"),
    USERS_SET_UNAME_NAME_ONE("UPDATE users SET username = ?, name = ? WHERE id = ?"),
    // Admin only user stuff
    USERS_ADMIN_SET_ADMIN_ONE("UPDATE users SET admin = ? WHERE id = ?"),
    USERS_ADMIN_SET_UNAME_ADMIN_ONE("UPDATE users SET username = ?, admin = ? WHERE id = ?"),
    USERS_ADMIN_SET_NAME_ADMIN_ONE("UPDATE users SET name = ?, admin = ? WHERE id = ?"),
    USERS_ADMIN_SET_UNAME_NAME_ADMIN_ONE("UPDATE users SET username = ?, name = ?, admin = ? WHERE id = ?"),
    USERS_ADMIN_DEL_ONE("DELETE FROM users WHERE id = ?"),
    // Article stuff
    ART_GET_COUNT("SELECT COUNT(id) AS 'count' FROM articles WHERE HIDDEN IS FALSE"),
    ART_GET_COUNT_BY_CAT("SELECT COUNT(id) AS 'count' FROM articles WHERE HIDDEN IS FALSE AND category_id = ?"),
    ART_ADMIN_GET_COUNT("SELECT COUNT(id) AS 'count' FROM articles"),
    ART_ADMIN_GET_COUNT_BY_CAT("SELECT COUNT(id) AS 'count' FROM articles WHERE category_id = ?"),
    ART_GET_ONE("SELECT articles.id AS 'id', category_id, categories.name AS 'category_name', UNIX_TIMESTAMP(made) AS 'made', UNIX_TIMESTAMP(lastEdited) AS 'lastEdited', title, content, hidden FROM articles LEFT JOIN categories ON articles.category_id = categories.id WHERE hidden IS FALSE AND articles.id = ?"),
    ART_GET_ONE_TRUNC("SELECT articles.id AS 'id', category_id, categories.name AS 'category_name', UNIX_TIMESTAMP(made) AS 'made', UNIX_TIMESTAMP(lastEdited) AS 'lastEdited', title, CASE WHEN CHAR_LENGTH(content) > ? THEN CONCAT(SUBSTRING(content, 1, ?), '...') ELSE content END AS 'content', hidden FROM articles LEFT JOIN categories ON articles.category_id = categories.id WHERE hidden IS FALSE AND articles.id = ?"),
    ART_GET_ALL("SELECT articles.id AS 'id', category_id, categories.name AS 'category_name', UNIX_TIMESTAMP(made) AS 'made', UNIX_TIMESTAMP(lastEdited) AS 'lastEdited', title, content, hidden FROM articles LEFT JOIN categories ON articles.category_id = categories.id WHERE hidden IS FALSE ORDER BY lastEdited DESC"),
    ART_GET_ALL_TRUNC("SELECT articles.id AS 'id', category_id, categories.name AS 'category_name', UNIX_TIMESTAMP(made) AS 'made', UNIX_TIMESTAMP(lastEdited) AS 'lastEdited', title, CASE WHEN CHAR_LENGTH(content) > ? THEN CONCAT(SUBSTRING(content, 1, ?), '...') ELSE content END AS 'content', hidden FROM articles LEFT JOIN categories ON articles.category_id = categories.id WHERE hidden IS FALSE ORDER BY lastEdited DESC"),
    ART_GET_ALL_BY_CAT("SELECT articles.id AS 'id', category_id, categories.name AS 'category_name', UNIX_TIMESTAMP(made) AS 'made', UNIX_TIMESTAMP(lastEdited) AS 'lastEdited', title, content, hidden FROM articles LEFT JOIN categories ON articles.category_id = categories.id WHERE hidden IS FALSE AND category_id = ? ORDER BY lastEdited DESC"),
    ART_GET_ALL_TRUNC_BY_CAT("SELECT articles.id AS 'id', category_id, categories.name AS 'category_name', UNIX_TIMESTAMP(made) AS 'made', UNIX_TIMESTAMP(lastEdited) AS 'lastEdited', title, CASE WHEN CHAR_LENGTH(content) > ? THEN CONCAT(SUBSTRING(content, 1, ?), '...') ELSE content END AS 'content', hidden FROM articles LEFT JOIN categories ON articles.category_id = categories.id WHERE hidden IS FALSE AND category_id = ? ORDER BY lastEdited DESC"),
    ART_GET_SOME_BY_CAT_EDITED_RANGE("SELECT articles.id AS 'id', category_id, categories.name AS 'category_name', UNIX_TIMESTAMP(made) AS 'made', UNIX_TIMESTAMP(lastEdited) AS 'lastEdited', title, content, hidden FROM articles LEFT JOIN categories ON articles.category_id = categories.id WHERE hidden IS FALSE AND category_id = ? ORDER BY lastEdited DESC LIMIT ? OFFSET ?"),
    ART_GET_SOME_TRUNC_BY_CAT_EDITED_RANGE("SELECT articles.id AS 'id', category_id, categories.name AS 'category_name', UNIX_TIMESTAMP(made) AS 'made', UNIX_TIMESTAMP(lastEdited) AS 'lastEdited', title, CASE WHEN CHAR_LENGTH(content) > ? THEN CONCAT(SUBSTRING(content, 1, ?), '...') ELSE content END AS 'content', hidden FROM articles LEFT JOIN categories ON articles.category_id = categories.id WHERE hidden IS FALSE AND category_id = ? ORDER BY lastEdited DESC LIMIT ? OFFSET ?"),
    ART_GET_SOME_BY_EDITED_RANGE("SELECT articles.id AS 'id', category_id, categories.name AS 'category_name', UNIX_TIMESTAMP(made) AS 'made', UNIX_TIMESTAMP(lastEdited) AS 'lastEdited', title, content, hidden FROM articles LEFT JOIN categories ON articles.category_id = categories.id WHERE hidden IS FALSE ORDER BY lastEdited DESC LIMIT ? OFFSET ?"),
    ART_GET_SOME_TRUNC_BY_EDITED_RANGE("SELECT articles.id AS 'id', category_id, categories.name AS 'category_name', UNIX_TIMESTAMP(made) AS 'made', UNIX_TIMESTAMP(lastEdited) AS 'lastEdited', title, CASE WHEN CHAR_LENGTH(content) > ? THEN CONCAT(SUBSTRING(content, 1, ?), '...') ELSE content END AS 'content', hidden FROM articles LEFT JOIN categories ON articles.category_id = categories.id WHERE hidden IS FALSE ORDER BY lastEdited DESC LIMIT ? OFFSET ?"),
    ART_GET_COUNT_BY_MATCHING("SELECT count(id) AS 'count' FROM articles WHERE hidden IS FALSE AND MATCH (title, content) AGAINST (? WITH QUERY EXPANSION)"),
    ART_GET_SOME_BY_MATCHING("SELECT articles.id AS 'id', category_id, categories.name AS 'category_name', UNIX_TIMESTAMP(made) AS 'made', UNIX_TIMESTAMP(lastEdited) AS 'lastEdited', title, CASE WHEN CHAR_LENGTH(content) > ? THEN CONCAT(SUBSTRING(content, 1, ?), '...') ELSE content END AS 'content', hidden, MATCH (title, content) AGAINST (? WITH QUERY EXPANSION) AS 'relevance' FROM articles LEFT JOIN categories ON articles.category_id = categories.id WHERE hidden IS FALSE AND MATCH (title, content) AGAINST (? WITH QUERY EXPANSION) ORDER BY 'relevance' DESC LIMIT ? OFFSET ?"),
    ART_ADMIN_MAKE_ONE("INSERT INTO articles (category_id, title, content, hidden) VALUES (?, ?, ?, ?)"),
    ART_ADMIN_GET_COUNT_HIDDEN("SELECT COUNT(id) AS 'count' FROM articles WHERE hidden IS TRUE"),
    ART_ADMIN_GET_COUNT_HIDDEN_BY_CAT("SELECT COUNT(id) AS 'count' FROM articles WHERE HIDDEN IS TRUE AND category_id = ?"),
    ART_ADMIN_GET_ONE_HIDDEN("SELECT articles.id AS 'id', category_id, categories.name AS 'category_name', UNIX_TIMESTAMP(made) AS 'made', UNIX_TIMESTAMP(lastEdited) AS 'lastEdited', title, content, hidden FROM articles LEFT JOIN categories ON articles.category_id = categories.id WHERE hidden IS TRUE AND articles.id = ?"),
    ART_ADMIN_GET_ONE_HIDDEN_TRUNC("SELECT articles.id AS 'id', category_id, categories.name AS 'category_name', UNIX_TIMESTAMP(made) AS 'made', UNIX_TIMESTAMP(lastEdited) AS 'lastEdited', title, CASE WHEN CHAR_LENGTH(content) > ? THEN CONCAT(SUBSTRING(content, 1, ?), '...') ELSE content END AS 'content', hidden FROM articles LEFT JOIN categories ON articles.category_id = categories.id WHERE hidden IS TRUE AND articles.id = ?"),
    ART_ADMIN_GET_ALL_HIDDEN("SELECT articles.id AS 'id', category_id, categories.name AS 'category_name', UNIX_TIMESTAMP(made) AS 'made', UNIX_TIMESTAMP(lastEdited) AS 'lastEdited', title, content, hidden FROM articles LEFT JOIN categories ON articles.category_id = categories.id WHERE hidden IS TRUE ORDER BY lastEdited DESC"),
    ART_ADMIN_GET_ALL_HIDDEN_TRUNC("SELECT articles.id AS 'id', category_id, categories.name AS 'category_name', UNIX_TIMESTAMP(made) AS 'made', UNIX_TIMESTAMP(lastEdited) AS 'lastEdited', title, CASE WHEN CHAR_LENGTH(content) > ? THEN CONCAT(SUBSTRING(content, 1, ?), '...') ELSE content END AS 'content', hidden FROM articles LEFT JOIN categories ON articles.category_id = categories.id WHERE hidden IS TRUE ORDER BY lastEdited DESC"),
    ART_ADMIN_GET_ALL_HIDDEN_BY_CAT("SELECT articles.id AS 'id', category_id, categories.name AS 'category_name', UNIX_TIMESTAMP(made) AS 'made', UNIX_TIMESTAMP(lastEdited) AS 'lastEdited', title, content, hidden FROM articles LEFT JOIN categories ON articles.category_id = categories.id WHERE hidden IS TRUE AND category_id = ? ORDER BY lastEdited DESC"),
    ART_ADMIN_GET_ALL_HIDDEN_TRUNC_BY_CAT("SELECT articles.id AS 'id', category_id, categories.name AS 'category_name', UNIX_TIMESTAMP(made) AS 'made', UNIX_TIMESTAMP(lastEdited) AS 'lastEdited', title, CASE WHEN CHAR_LENGTH(content) > ? THEN CONCAT(SUBSTRING(content, 1, ?), '...') ELSE content END AS 'content', hidden FROM articles LEFT JOIN categories ON articles.category_id = categories.id WHERE hidden IS TRUE AND category_id = ? ORDER BY lastEdited DESC"),
    ART_ADMIN_GET_SOME_HIDDEN_BY_CAT_EDITED_RANGE("SELECT articles.id AS 'id', category_id, categories.name AS 'category_name', UNIX_TIMESTAMP(made) AS 'made', UNIX_TIMESTAMP(lastEdited) AS 'lastEdited', title, content, hidden FROM articles LEFT JOIN categories ON articles.category_id = categories.id WHERE hidden IS TRUE AND category_id = ? ORDER BY lastEdited DESC LIMIT ? OFFSET ?"),
    ART_ADMIN_GET_SOME_HIDDEN_TRUNC_BY_CAT_EDITED_RANGE("SELECT articles.id AS 'id', category_id, categories.name AS 'category_name', UNIX_TIMESTAMP(made) AS 'made', UNIX_TIMESTAMP(lastEdited) AS 'lastEdited', title, CASE WHEN CHAR_LENGTH(content) > ? THEN CONCAT(SUBSTRING(content, 1, ?), '...') ELSE content END AS 'content', hidden FROM articles LEFT JOIN categories ON articles.category_id = categories.id WHERE hidden IS TRUE AND category_id = ? ORDER BY lastEdited DESC LIMIT ? OFFSET ?"),
    ART_ADMIN_GET_SOME_HIDDEN_BY_EDITED_RANGE("SELECT articles.id AS 'id', category_id, categories.name AS 'category_name', UNIX_TIMESTAMP(made) AS 'made', UNIX_TIMESTAMP(lastEdited) AS 'lastEdited', title, content, hidden FROM articles LEFT JOIN categories ON articles.category_id = categories.id WHERE hidden IS TRUE ORDER BY lastEdited DESC LIMIT ? OFFSET ?"),
    ART_ADMIN_GET_SOME_HIDDEN_TRUNC_BY_EDITED_RANGE("SELECT articles.id AS 'id', category_id, categories.name AS 'category_name', UNIX_TIMESTAMP(made) AS 'made', UNIX_TIMESTAMP(lastEdited) AS 'lastEdited', title, CASE WHEN CHAR_LENGTH(content) > ? THEN CONCAT(SUBSTRING(content, 1, ?), '...') ELSE content END AS 'content', hidden FROM articles LEFT JOIN categories ON articles.category_id = categories.id WHERE hidden IS TRUE ORDER BY lastEdited DESC LIMIT ? OFFSET ?"),
    ART_ADMIN_GET_COUNT_HIDDEN_BY_MATCHING("SELECT count(id) AS 'count' FROM articles WHERE hidden IS TRUE AND MATCH (title, content) AGAINST (? WITH QUERY EXPANSION)"),
    ART_ADMIN_GET_SOME_HIDDEN_BY_MATCHING("SELECT articles.id AS 'id', category_id, categories.name AS 'category_name', UNIX_TIMESTAMP(made) AS 'made', UNIX_TIMESTAMP(lastEdited) AS 'lastEdited', title, CASE WHEN CHAR_LENGTH(content) > ? THEN CONCAT(SUBSTRING(content, 1, ?), '...') ELSE content END AS 'content', hidden, MATCH (title, content) AGAINST (? WITH QUERY EXPANSION) AS 'relevance' FROM articles LEFT JOIN categories ON articles.category_id = categories.id WHERE hidden IS TRUE AND MATCH (title, content) AGAINST (? WITH QUERY EXPANSION) ORDER BY 'relevance' DESC LIMIT ? OFFSET ?"),
    ART_ADMIN_GET_ONE("SELECT articles.id AS 'id', category_id, categories.name AS 'category_name', UNIX_TIMESTAMP(made) AS 'made', UNIX_TIMESTAMP(lastEdited) AS 'lastEdited', title, content, hidden FROM articles LEFT JOIN categories ON articles.category_id = categories.id WHERE articles.id = ?"),
    ART_ADMIN_GET_ONE_TRUNC("SELECT articles.id AS 'id', category_id, categories.name AS 'category_name', UNIX_TIMESTAMP(made) AS 'made', UNIX_TIMESTAMP(lastEdited) AS 'lastEdited', title, CASE WHEN CHAR_LENGTH(content) > ? THEN CONCAT(SUBSTRING(content, 1, ?), '...') ELSE content END AS 'content', hidden FROM articles LEFT JOIN categories ON articles.category_id = categories.id WHERE articles.id = ?"),
    ART_ADMIN_GET_ALL("SELECT articles.id AS 'id', category_id, categories.name AS 'category_name', UNIX_TIMESTAMP(made) AS 'made', UNIX_TIMESTAMP(lastEdited) AS 'lastEdited', title, content, hidden FROM articles LEFT JOIN categories ON articles.category_id = categories.id ORDER BY lastEdited DESC"),
    ART_ADMIN_GET_ALL_TRUNC("SELECT articles.id AS 'id', category_id, categories.name AS 'category_name', UNIX_TIMESTAMP(made) AS 'made', UNIX_TIMESTAMP(lastEdited) AS 'lastEdited', title, CASE WHEN CHAR_LENGTH(content) > ? THEN CONCAT(SUBSTRING(content, 1, ?), '...') ELSE content END AS 'content', hidden FROM articles LEFT JOIN categories ON articles.category_id = categories.id ORDER BY lastEdited DESC"),
    ART_ADMIN_GET_ALL_BY_CAT("SELECT articles.id AS 'id', category_id, categories.name AS 'category_name', UNIX_TIMESTAMP(made) AS 'made', UNIX_TIMESTAMP(lastEdited) AS 'lastEdited', title, content, hidden FROM articles LEFT JOIN categories ON articles.category_id = categories.id WHERE category_id = ? ORDER BY lastEdited DESC"),
    ART_ADMIN_GET_ALL_TRUNC_BY_CAT("SELECT articles.id AS 'id', category_id, categories.name AS 'category_name', UNIX_TIMESTAMP(made) AS 'made', UNIX_TIMESTAMP(lastEdited) AS 'lastEdited', title, CASE WHEN CHAR_LENGTH(content) > ? THEN CONCAT(SUBSTRING(content, 1, ?), '...') ELSE content END AS 'content', hidden FROM articles LEFT JOIN categories ON articles.category_id = categories.id WHERE category_id = ? ORDER BY lastEdited DESC"),
    ART_ADMIN_GET_SOME_BY_CAT_EDITED_RANGE("SELECT articles.id AS 'id', category_id, categories.name AS 'category_name', UNIX_TIMESTAMP(made) AS 'made', UNIX_TIMESTAMP(lastEdited) AS 'lastEdited', title, content, hidden FROM articles LEFT JOIN categories ON articles.category_id = categories.id WHERE category_id = ? ORDER BY lastEdited DESC LIMIT ? OFFSET ?"),
    ART_ADMIN_GET_SOME_TRUNC_BY_CAT_EDITED_RANGE("SELECT articles.id AS 'id', category_id, categories.name AS 'category_name', UNIX_TIMESTAMP(made) AS 'made', UNIX_TIMESTAMP(lastEdited) AS 'lastEdited', title, CASE WHEN CHAR_LENGTH(content) > ? THEN CONCAT(SUBSTRING(content, 1, ?), '...') ELSE content END AS 'content', hidden FROM articles LEFT JOIN categories ON articles.category_id = categories.id WHERE category_id = ? ORDER BY lastEdited DESC LIMIT ? OFFSET ?"),
    ART_ADMIN_GET_SOME_BY_EDITED_RANGE("SELECT articles.id AS 'id', category_id, categories.name AS 'category_name', UNIX_TIMESTAMP(made) AS 'made', UNIX_TIMESTAMP(lastEdited) AS 'lastEdited', title, content, hidden FROM articles LEFT JOIN categories ON articles.category_id = categories.id ORDER BY lastEdited DESC LIMIT ? OFFSET ?"),
    ART_ADMIN_GET_SOME_TRUNC_BY_EDITED_RANGE("SELECT articles.id AS 'id', category_id, categories.name AS 'category_name', UNIX_TIMESTAMP(made) AS 'made', UNIX_TIMESTAMP(lastEdited) AS 'lastEdited', title, CASE WHEN CHAR_LENGTH(content) > ? THEN CONCAT(SUBSTRING(content, 1, ?), '...') ELSE content END AS 'content', hidden FROM articles LEFT JOIN categories ON articles.category_id = categories.id ORDER BY lastEdited DESC LIMIT ? OFFSET ?"),
    ART_ADMIN_GET_COUNT_BY_MATCHING("SELECT count(id) AS 'count' FROM articles WHERE MATCH (title, content) AGAINST (? WITH QUERY EXPANSION)"),
    ART_ADMIN_GET_SOME_BY_MATCHING("SELECT articles.id AS 'id', category_id, categories.name AS 'category_name', UNIX_TIMESTAMP(made) AS 'made', UNIX_TIMESTAMP(lastEdited) AS 'lastEdited', title, CASE WHEN CHAR_LENGTH(content) > ? THEN CONCAT(SUBSTRING(content, 1, ?), '...') ELSE content END AS 'content', hidden, MATCH (title, content) AGAINST (? WITH QUERY EXPANSION) AS 'relevance' FROM articles LEFT JOIN categories ON articles.category_id = categories.id WHERE MATCH (title, content) AGAINST (? WITH QUERY EXPANSION) ORDER BY 'relevance' DESC LIMIT ? OFFSET ?"),
    ART_ADMIN_SET_CAT_ONE("UPDATE articles SET category_id = ? WHERE id = ?"),
    ART_ADMIN_SET_TITLE_ONE("UPDATE articles SET title = ? WHERE id = ?"),
    ART_ADMIN_SET_CONTENT_ONE("UPDATE articles SET content = ? WHERE id = ?"),
    ART_ADMIN_SET_HIDDEN_ONE("UPDATE articles SET hidden = ? WHERE id = ?"),
    ART_ADMIN_SET_CAT_TITLE_ONE("UPDATE articles SET category_id = ?, title = ? WHERE id = ?"),
    ART_ADMIN_SET_CAT_CONTENT_ONE("UPDATE articles SET category_id = ?, content = ? WHERE id = ?"),
    ART_ADMIN_SET_CAT_HIDDEN_ONE("UPDATE articles SET category_id = ?, hidden = ? WHERE id = ?"),
    ART_ADMIN_SET_TITLE_CONTENT_ONE("UPDATE articles SET title = ?, content = ? WHERE id = ?"),
    ART_ADMIN_SET_TITLE_HIDDEN_ONE("UPDATE articles SET title = ?, hidden = ? WHERE id = ?"),
    ART_ADMIN_SET_CONTENT_HIDDEN_ONE("UPDATE articles SET content = ?, hidden = ? WHERE id = ?"),
    ART_ADMIN_SET_CAT_TITLE_CONTENT_ONE("UPDATE articles SET category_id = ?, title = ?, content = ? WHERE id = ?"),
    ART_ADMIN_SET_CAT_TITLE_HIDDEN_ONE("UPDATE articles SET category_id = ?, title = ?, hidden = ? WHERE id = ?"),
    ART_ADMIN_SET_CAT_CONTENT_HIDDEN_ONE("UPDATE articles SET category_id = ?, content = ?, hidden = ? WHERE id = ?"),
    ART_ADMIN_SET_TITLE_CONTENT_HIDDEN_ONE("UPDATE articles SET title = ?, content = ?, hidden = ? WHERE id = ?"),
    ART_ADMIN_SET_CAT_TITLE_CONTENT_HIDDEN_ONE("UPDATE articles SET category_id = ?, title = ?, content = ?, hidden = ? WHERE id = ?"),
    ART_ADMIN_DEL_ONE("DELETE FROM articles WHERE id = ?"),
    // Category stuff
    CAT_GET_COUNT("SELECT COUNT(id) AS 'count' FROM categories"),
    CAT_GET_ONE("SELECT id, name FROM categories WHERE id = ?"),
    CAT_GET_ALL("SELECT id, name FROM categories"),
    CAT_ADMIN_MAKE_ONE("INSERT INTO categories (name) VALUES (?)"),
    CAT_ADMIN_SET_NAME_ONE("UPDATE categories SET name = ? WHERE id = ?"),
    CAT_ADMIN_DEL_ONE("DELETE FROM categories WHERE id = ?"),
    USERS_GET_ADMIN_ONE("SELECT admin FROM users WHERE id = ?"),
    ADMIN_GET_LAST_ID("SELECT LAST_INSERT_ID() AS 'id'");
    // FUCKING FINALLY

    private final String statement;


    SQLStatement(String statement) {
        this.statement = statement;
    }

    public String getStatement() {
        return statement;
    }
}
