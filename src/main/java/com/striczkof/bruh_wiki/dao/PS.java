package com.striczkof.bruh_wiki.dao;

/**
 * This enum is used as a selector for SQL statements. For initialisation of prepared statements, use {@link SQLStatement}.
 * The constants of this and {@link SQLStatement} must be named the same.
 * Constant names: table name_(admin if admin only)_(make/get/set/del)_(specific columns)_number_modifier_(by_value for where except none or only id)
 * @see SQLStatement
 */
public enum PS {
    // User stuff

    /**
     * Get the number of users
     * @see SQLStatement#USERS_GET_COUNT
     */
    USERS_GET_COUNT,

    /**
     * get one user, param 1 for id
     * @see SQLStatement#USERS_GET_ONE
     */
    USERS_GET_ONE,

    /**
     * Get all users
     * @see SQLStatement#USERS_GET_ALL
     */
    USERS_GET_ALL,

    /**
     * Register a user, param 1 for username, param 2 for hash, param 3 for salt, param 4 for name
     * @see SQLStatement#USERS_MAKE_ONE
     */
    USERS_MAKE_ONE,

    /**
     * Get user id and salt, param 1 for username
     * @see SQLStatement#USERS_GET_ID_SALT_ONE_BY_UNAME
     */
    USERS_GET_ID_SALT_ONE_BY_UNAME,

    /**
     * Set last login to current time, param 1 for id, param 2 for hash
     * @see SQLStatement#USERS_SET_LAST_LOGIN_ONE_BY_ID_HASH
     */
    USERS_SET_LAST_LOGIN_ONE_BY_ID_HASH,

    /**
     * Set new password, param 1 for new hash, param 2 for new salt, param 3 for id, param 4 for old hash
     * @see SQLStatement#USERS_SET_PASSWORD_ONE_BY_ID_HASH
     */
    USERS_SET_PASSWORD_ONE_BY_ID_HASH,

    /**
     * Delete one user, param 1 for id, param 2 for hash
     * @see SQLStatement#USERS_DEL_ONE_BY_ID_HASH
     */
    USERS_DEL_ONE_BY_ID_HASH,

    /**
     * Get user id and username, param 1 for text in username
     * @see SQLStatement#USERS_GET_ID_UNAME_ONE_LIKE_UNAME
     */
    USERS_GET_ID_UNAME_ONE_LIKE_UNAME,

    /**
     * Get user id, param 1 for username
     * @see SQLStatement#USERS_GET_ID_ONE_BY_UNAME
     */
    USERS_GET_ID_ONE_BY_UNAME,

    // User stuff for both user and admin

    /**
     * Set username, param 1 for new username, param 2 for id
     * @see SQLStatement#USERS_SET_UNAME_ONE
     */
    USERS_SET_UNAME_ONE,

    /**
     * Set name, param 1 for new name, param 2 for id
     * @see SQLStatement#USERS_SET_NAME_ONE
     */
    USERS_SET_NAME_ONE,

    /**
     * @see SQLStatement#USERS_SET_UNAME_NAME_ONE
     */
    USERS_SET_UNAME_NAME_ONE,

    // Admin only user stuff

    /**
     * @see SQLStatement#USERS_ADMIN_SET_ADMIN_ONE
     */
    USERS_ADMIN_SET_ADMIN_ONE,

    /**
     * @see SQLStatement#USERS_ADMIN_SET_UNAME_ADMIN_ONE
     */
    USERS_ADMIN_SET_UNAME_ADMIN_ONE,

    /**
     * @see SQLStatement#USERS_ADMIN_SET_NAME_ADMIN_ONE
     */
    USERS_ADMIN_SET_NAME_ADMIN_ONE,

    /**
     * @see SQLStatement#USERS_ADMIN_SET_UNAME_NAME_ADMIN_ONE
     */
    USERS_ADMIN_SET_UNAME_NAME_ADMIN_ONE,

    /**
     * @see SQLStatement#USERS_ADMIN_DEL_ONE
     */
    USERS_ADMIN_DEL_ONE,

    // Article stuff

    /**
     * @see SQLStatement#ART_GET_COUNT
     */
    ART_GET_COUNT,

    /**
     * @see SQLStatement#ART_GET_COUNT_BY_CAT
     */
    ART_GET_COUNT_BY_CAT,

    /**
     * @see SQLStatement#ART_ADMIN_GET_COUNT
     */
    ART_ADMIN_GET_COUNT,

    /**
     * @see SQLStatement#ART_ADMIN_GET_COUNT_BY_CAT
     */
    ART_ADMIN_GET_COUNT_BY_CAT,

    /**
     * @see SQLStatement#ART_GET_ONE
     */
    ART_GET_ONE,

    /**
     * @see SQLStatement#ART_GET_ONE_TRUNC
     */
    ART_GET_ONE_TRUNC,

    /**
     * @see SQLStatement#ART_GET_ALL
     */
    ART_GET_ALL,

    /**
     * @see SQLStatement#ART_GET_ALL_TRUNC
     */
    ART_GET_ALL_TRUNC,

    /**
     * @see SQLStatement#ART_GET_ALL_BY_CAT
     */
    ART_GET_ALL_BY_CAT,

    /**
     * @see SQLStatement#ART_GET_ALL_TRUNC_BY_CAT
     */
    ART_GET_ALL_TRUNC_BY_CAT,

    /**
     * @see SQLStatement#ART_GET_SOME_BY_CAT_EDITED_RANGE
     */
    ART_GET_SOME_BY_CAT_EDITED_RANGE,

    /**
     * @see SQLStatement#ART_GET_SOME_TRUNC_BY_CAT_EDITED_RANGE
     */
    ART_GET_SOME_TRUNC_BY_CAT_EDITED_RANGE,


    /**
     * @see SQLStatement#ART_GET_SOME_BY_EDITED_RANGE
     */
    ART_GET_SOME_BY_EDITED_RANGE,

    /**
     * @see SQLStatement#ART_GET_SOME_TRUNC_BY_EDITED_RANGE
     */
    ART_GET_SOME_TRUNC_BY_EDITED_RANGE,

    /**
     * @see SQLStatement#ART_GET_COUNT_BY_MATCHING
     */
    ART_GET_COUNT_BY_MATCHING,

    /**
     * Already truncating!
     * @see SQLStatement#ART_GET_SOME_BY_MATCHING
     */
    ART_GET_SOME_BY_MATCHING,

    /**
     * @see SQLStatement#ART_ADMIN_MAKE_ONE
     */
    ART_ADMIN_MAKE_ONE,

    /**
     * @see SQLStatement#ART_ADMIN_GET_COUNT_HIDDEN
     */
    ART_ADMIN_GET_COUNT_HIDDEN,

    /**
     * @see SQLStatement#ART_ADMIN_GET_COUNT_HIDDEN_BY_CAT
     */
    ART_ADMIN_GET_COUNT_HIDDEN_BY_CAT,

    /**
     * @see SQLStatement#ART_ADMIN_GET_ONE_HIDDEN
     */
    ART_ADMIN_GET_ONE_HIDDEN,

    /**
     * @see SQLStatement#ART_ADMIN_GET_ONE_HIDDEN_TRUNC
     */
    ART_ADMIN_GET_ONE_HIDDEN_TRUNC,

    /**
     * @see SQLStatement#ART_ADMIN_GET_ALL_HIDDEN
     */
    ART_ADMIN_GET_ALL_HIDDEN,

    /**
     * @see SQLStatement#ART_ADMIN_GET_ALL_HIDDEN_TRUNC
     */
    ART_ADMIN_GET_ALL_HIDDEN_TRUNC,

    /**
     * @see SQLStatement#ART_ADMIN_GET_ALL_HIDDEN_BY_CAT
     */
    ART_ADMIN_GET_ALL_HIDDEN_BY_CAT,

    /**
     * @see SQLStatement#ART_ADMIN_GET_ALL_HIDDEN_TRUNC_BY_CAT
     */
    ART_ADMIN_GET_ALL_HIDDEN_TRUNC_BY_CAT,

    /**
     * @see SQLStatement#ART_ADMIN_GET_SOME_HIDDEN_BY_CAT_EDITED_RANGE
     */
    ART_ADMIN_GET_SOME_HIDDEN_BY_CAT_EDITED_RANGE,

    /**
     * @see SQLStatement#ART_ADMIN_GET_SOME_HIDDEN_TRUNC_BY_CAT_EDITED_RANGE
     */
    ART_ADMIN_GET_SOME_HIDDEN_TRUNC_BY_CAT_EDITED_RANGE,


    /**
     * @see SQLStatement#ART_ADMIN_GET_SOME_HIDDEN_BY_EDITED_RANGE
     */
    ART_ADMIN_GET_SOME_HIDDEN_BY_EDITED_RANGE,

    /**
     * @see SQLStatement#ART_ADMIN_GET_SOME_HIDDEN_TRUNC_BY_EDITED_RANGE
     */
    ART_ADMIN_GET_SOME_HIDDEN_TRUNC_BY_EDITED_RANGE,

    /**
     * @see SQLStatement#ART_ADMIN_GET_COUNT_HIDDEN_BY_MATCHING
     */
    ART_ADMIN_GET_COUNT_HIDDEN_BY_MATCHING,

    /**
     * @see SQLStatement#ART_ADMIN_GET_SOME_HIDDEN_BY_MATCHING
     */
    ART_ADMIN_GET_SOME_HIDDEN_BY_MATCHING,

    /**
     * @see SQLStatement#ART_ADMIN_GET_ONE
     */
    ART_ADMIN_GET_ONE,

    /**
     * @see SQLStatement#ART_ADMIN_GET_ONE_TRUNC
     */
    ART_ADMIN_GET_ONE_TRUNC,

    /**
     * @see SQLStatement#ART_ADMIN_GET_ALL
     */
    ART_ADMIN_GET_ALL,

    /**
     * @see SQLStatement#ART_ADMIN_GET_ALL_TRUNC
     */
    ART_ADMIN_GET_ALL_TRUNC,

    /**
     * @see SQLStatement#ART_ADMIN_GET_ALL_BY_CAT
     */
    ART_ADMIN_GET_ALL_BY_CAT,

    /**
     * @see SQLStatement#ART_ADMIN_GET_ALL_TRUNC_BY_CAT
     */
    ART_ADMIN_GET_ALL_TRUNC_BY_CAT,

    /**
     * @see SQLStatement#ART_ADMIN_GET_SOME_BY_CAT_EDITED_RANGE
     */
    ART_ADMIN_GET_SOME_BY_CAT_EDITED_RANGE,

    /**
     * @see SQLStatement#ART_ADMIN_GET_SOME_TRUNC_BY_CAT_EDITED_RANGE
     */
    ART_ADMIN_GET_SOME_TRUNC_BY_CAT_EDITED_RANGE,

    /**
     * @see SQLStatement#ART_ADMIN_GET_SOME_BY_MATCHING
     */
    ART_ADMIN_GET_SOME_BY_EDITED_RANGE,

    /**
     * @see SQLStatement#ART_ADMIN_GET_SOME_TRUNC_BY_EDITED_RANGE
     */
    ART_ADMIN_GET_SOME_TRUNC_BY_EDITED_RANGE,

    /**
     * @see SQLStatement#ART_ADMIN_GET_COUNT_BY_MATCHING
     */
    ART_ADMIN_GET_COUNT_BY_MATCHING,

    /**
     * @see SQLStatement#ART_ADMIN_GET_SOME_BY_MATCHING
     */
    ART_ADMIN_GET_SOME_BY_MATCHING,

    /**
     * @see SQLStatement#ART_ADMIN_SET_CAT_ONE
     */
    ART_ADMIN_SET_CAT_ONE,

    /**
     * @see SQLStatement#ART_ADMIN_SET_TITLE_ONE
     */
    ART_ADMIN_SET_TITLE_ONE,

    /**
     * @see SQLStatement#ART_ADMIN_SET_CONTENT_ONE
     */
    ART_ADMIN_SET_CONTENT_ONE,

    /**
     * @see SQLStatement#ART_ADMIN_SET_HIDDEN_ONE
     */
    ART_ADMIN_SET_HIDDEN_ONE,

    /**
     * @see SQLStatement#ART_ADMIN_SET_CAT_TITLE_ONE
     */
    ART_ADMIN_SET_CAT_TITLE_ONE,

    /**
     * @see SQLStatement#ART_ADMIN_SET_CAT_CONTENT_ONE
     */
    ART_ADMIN_SET_CAT_CONTENT_ONE,

    /**
     * @see SQLStatement#ART_ADMIN_SET_CAT_HIDDEN_ONE
     */
    ART_ADMIN_SET_CAT_HIDDEN_ONE,

    /**
     * @see SQLStatement#ART_ADMIN_SET_TITLE_CONTENT_ONE
     */
    ART_ADMIN_SET_TITLE_CONTENT_ONE,

    /**
     * @see SQLStatement#ART_ADMIN_SET_TITLE_HIDDEN_ONE
     */
    ART_ADMIN_SET_TITLE_HIDDEN_ONE,

    /**
     * @see SQLStatement#ART_ADMIN_SET_CONTENT_HIDDEN_ONE
     */
    ART_ADMIN_SET_CONTENT_HIDDEN_ONE,

    /**
     * @see SQLStatement#ART_ADMIN_SET_CAT_TITLE_CONTENT_ONE
     */
    ART_ADMIN_SET_CAT_TITLE_CONTENT_ONE,

    /**
     * @see SQLStatement#ART_ADMIN_SET_CAT_TITLE_HIDDEN_ONE
     */
    ART_ADMIN_SET_CAT_TITLE_HIDDEN_ONE,

    /**
     * @see SQLStatement#ART_ADMIN_SET_CAT_CONTENT_HIDDEN_ONE
     */
    ART_ADMIN_SET_CAT_CONTENT_HIDDEN_ONE,

    /**
     * @see SQLStatement#ART_ADMIN_SET_TITLE_CONTENT_HIDDEN_ONE
     */
    ART_ADMIN_SET_TITLE_CONTENT_HIDDEN_ONE,

    /**
     * @see SQLStatement#ART_ADMIN_SET_CAT_TITLE_CONTENT_HIDDEN_ONE
     */
    ART_ADMIN_SET_CAT_TITLE_CONTENT_HIDDEN_ONE,

    /**
     * @see SQLStatement#ART_ADMIN_DEL_ONE
     */
    ART_ADMIN_DEL_ONE,

    // Category stuff

    /**
     * @see SQLStatement#CAT_GET_COUNT
     */
    CAT_GET_COUNT,

    /**
     * @see SQLStatement#CAT_GET_ONE
     */
    CAT_GET_ONE,

    /**
     * @see SQLStatement#CAT_GET_ALL
     */
    CAT_GET_ALL,

    /**
     * @see SQLStatement#CAT_ADMIN_MAKE_ONE
     */
    CAT_ADMIN_MAKE_ONE,

    /**
     * @see SQLStatement#CAT_ADMIN_SET_NAME_ONE
     */
    CAT_ADMIN_SET_NAME_ONE,

    /**
     * @see SQLStatement#CAT_ADMIN_DEL_ONE
     */
    CAT_ADMIN_DEL_ONE,

    /**
     * @see SQLStatement#USERS_GET_ADMIN_ONE
     */
    USERS_GET_ADMIN_ONE,

    /**
     * @see SQLStatement#ADMIN_GET_LAST_ID
     */
    ADMIN_GET_LAST_ID;
}
