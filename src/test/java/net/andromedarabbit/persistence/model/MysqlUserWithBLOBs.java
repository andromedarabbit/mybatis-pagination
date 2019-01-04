package net.andromedarabbit.persistence.model;

import java.io.Serializable;

public class MysqlUserWithBLOBs extends MysqlUser implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.ssl_cipher
     *
     * @mbggenerated
     */
    private byte[] sslCipher;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.x509_issuer
     *
     * @mbggenerated
     */
    private byte[] x509Issuer;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.x509_subject
     *
     * @mbggenerated
     */
    private byte[] x509Subject;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.authentication_string
     *
     * @mbggenerated
     */
    private String authenticationString;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table user
     *
     * @mbggenerated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.ssl_cipher
     *
     * @return the value of user.ssl_cipher
     *
     * @mbggenerated
     */
    public byte[] getSslCipher() {
        return sslCipher;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.ssl_cipher
     *
     * @param sslCipher the value for user.ssl_cipher
     *
     * @mbggenerated
     */
    public void setSslCipher(byte[] sslCipher) {
        this.sslCipher = sslCipher;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.x509_issuer
     *
     * @return the value of user.x509_issuer
     *
     * @mbggenerated
     */
    public byte[] getX509Issuer() {
        return x509Issuer;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.x509_issuer
     *
     * @param x509Issuer the value for user.x509_issuer
     *
     * @mbggenerated
     */
    public void setX509Issuer(byte[] x509Issuer) {
        this.x509Issuer = x509Issuer;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.x509_subject
     *
     * @return the value of user.x509_subject
     *
     * @mbggenerated
     */
    public byte[] getX509Subject() {
        return x509Subject;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.x509_subject
     *
     * @param x509Subject the value for user.x509_subject
     *
     * @mbggenerated
     */
    public void setX509Subject(byte[] x509Subject) {
        this.x509Subject = x509Subject;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.authentication_string
     *
     * @return the value of user.authentication_string
     *
     * @mbggenerated
     */
    public String getAuthenticationString() {
        return authenticationString;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.authentication_string
     *
     * @param authenticationString the value for user.authentication_string
     *
     * @mbggenerated
     */
    public void setAuthenticationString(String authenticationString) {
        this.authenticationString = authenticationString == null ? null : authenticationString.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user
     *
     * @mbggenerated
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        MysqlUserWithBLOBs other = (MysqlUserWithBLOBs) that;
        return (this.getHost() == null ? other.getHost() == null : this.getHost().equals(other.getHost()))
            && (this.getUser() == null ? other.getUser() == null : this.getUser().equals(other.getUser()))
            && (this.getPassword() == null ? other.getPassword() == null : this.getPassword().equals(other.getPassword()))
            && (this.getSelectPriv() == null ? other.getSelectPriv() == null : this.getSelectPriv().equals(other.getSelectPriv()))
            && (this.getInsertPriv() == null ? other.getInsertPriv() == null : this.getInsertPriv().equals(other.getInsertPriv()))
            && (this.getUpdatePriv() == null ? other.getUpdatePriv() == null : this.getUpdatePriv().equals(other.getUpdatePriv()))
            && (this.getDeletePriv() == null ? other.getDeletePriv() == null : this.getDeletePriv().equals(other.getDeletePriv()))
            && (this.getCreatePriv() == null ? other.getCreatePriv() == null : this.getCreatePriv().equals(other.getCreatePriv()))
            && (this.getDropPriv() == null ? other.getDropPriv() == null : this.getDropPriv().equals(other.getDropPriv()))
            && (this.getReloadPriv() == null ? other.getReloadPriv() == null : this.getReloadPriv().equals(other.getReloadPriv()))
            && (this.getShutdownPriv() == null ? other.getShutdownPriv() == null : this.getShutdownPriv().equals(other.getShutdownPriv()))
            && (this.getProcessPriv() == null ? other.getProcessPriv() == null : this.getProcessPriv().equals(other.getProcessPriv()))
            && (this.getFilePriv() == null ? other.getFilePriv() == null : this.getFilePriv().equals(other.getFilePriv()))
            && (this.getGrantPriv() == null ? other.getGrantPriv() == null : this.getGrantPriv().equals(other.getGrantPriv()))
            && (this.getReferencesPriv() == null ? other.getReferencesPriv() == null : this.getReferencesPriv().equals(other.getReferencesPriv()))
            && (this.getIndexPriv() == null ? other.getIndexPriv() == null : this.getIndexPriv().equals(other.getIndexPriv()))
            && (this.getAlterPriv() == null ? other.getAlterPriv() == null : this.getAlterPriv().equals(other.getAlterPriv()))
            && (this.getShowDbPriv() == null ? other.getShowDbPriv() == null : this.getShowDbPriv().equals(other.getShowDbPriv()))
            && (this.getSuperPriv() == null ? other.getSuperPriv() == null : this.getSuperPriv().equals(other.getSuperPriv()))
            && (this.getCreateTmpTablePriv() == null ? other.getCreateTmpTablePriv() == null : this.getCreateTmpTablePriv().equals(other.getCreateTmpTablePriv()))
            && (this.getLockTablesPriv() == null ? other.getLockTablesPriv() == null : this.getLockTablesPriv().equals(other.getLockTablesPriv()))
            && (this.getExecutePriv() == null ? other.getExecutePriv() == null : this.getExecutePriv().equals(other.getExecutePriv()))
            && (this.getReplSlavePriv() == null ? other.getReplSlavePriv() == null : this.getReplSlavePriv().equals(other.getReplSlavePriv()))
            && (this.getReplClientPriv() == null ? other.getReplClientPriv() == null : this.getReplClientPriv().equals(other.getReplClientPriv()))
            && (this.getCreateViewPriv() == null ? other.getCreateViewPriv() == null : this.getCreateViewPriv().equals(other.getCreateViewPriv()))
            && (this.getShowViewPriv() == null ? other.getShowViewPriv() == null : this.getShowViewPriv().equals(other.getShowViewPriv()))
            && (this.getCreateRoutinePriv() == null ? other.getCreateRoutinePriv() == null : this.getCreateRoutinePriv().equals(other.getCreateRoutinePriv()))
            && (this.getAlterRoutinePriv() == null ? other.getAlterRoutinePriv() == null : this.getAlterRoutinePriv().equals(other.getAlterRoutinePriv()))
            && (this.getCreateUserPriv() == null ? other.getCreateUserPriv() == null : this.getCreateUserPriv().equals(other.getCreateUserPriv()))
            && (this.getEventPriv() == null ? other.getEventPriv() == null : this.getEventPriv().equals(other.getEventPriv()))
            && (this.getTriggerPriv() == null ? other.getTriggerPriv() == null : this.getTriggerPriv().equals(other.getTriggerPriv()))
            && (this.getCreateTablespacePriv() == null ? other.getCreateTablespacePriv() == null : this.getCreateTablespacePriv().equals(other.getCreateTablespacePriv()))
            && (this.getSslType() == null ? other.getSslType() == null : this.getSslType().equals(other.getSslType()))
            && (this.getMaxQuestions() == null ? other.getMaxQuestions() == null : this.getMaxQuestions().equals(other.getMaxQuestions()))
            && (this.getMaxUpdates() == null ? other.getMaxUpdates() == null : this.getMaxUpdates().equals(other.getMaxUpdates()))
            && (this.getMaxConnections() == null ? other.getMaxConnections() == null : this.getMaxConnections().equals(other.getMaxConnections()))
            && (this.getMaxUserConnections() == null ? other.getMaxUserConnections() == null : this.getMaxUserConnections().equals(other.getMaxUserConnections()))
            && (this.getPlugin() == null ? other.getPlugin() == null : this.getPlugin().equals(other.getPlugin()))
            && (this.getPasswordExpired() == null ? other.getPasswordExpired() == null : this.getPasswordExpired().equals(other.getPasswordExpired()))
            && (this.getIsRole() == null ? other.getIsRole() == null : this.getIsRole().equals(other.getIsRole()))
            && (this.getSslCipher() == null ? other.getSslCipher() == null : this.getSslCipher().equals(other.getSslCipher()))
            && (this.getX509Issuer() == null ? other.getX509Issuer() == null : this.getX509Issuer().equals(other.getX509Issuer()))
            && (this.getX509Subject() == null ? other.getX509Subject() == null : this.getX509Subject().equals(other.getX509Subject()))
            && (this.getAuthenticationString() == null ? other.getAuthenticationString() == null : this.getAuthenticationString().equals(other.getAuthenticationString()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user
     *
     * @mbggenerated
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getHost() == null) ? 0 : getHost().hashCode());
        result = prime * result + ((getUser() == null) ? 0 : getUser().hashCode());
        result = prime * result + ((getPassword() == null) ? 0 : getPassword().hashCode());
        result = prime * result + ((getSelectPriv() == null) ? 0 : getSelectPriv().hashCode());
        result = prime * result + ((getInsertPriv() == null) ? 0 : getInsertPriv().hashCode());
        result = prime * result + ((getUpdatePriv() == null) ? 0 : getUpdatePriv().hashCode());
        result = prime * result + ((getDeletePriv() == null) ? 0 : getDeletePriv().hashCode());
        result = prime * result + ((getCreatePriv() == null) ? 0 : getCreatePriv().hashCode());
        result = prime * result + ((getDropPriv() == null) ? 0 : getDropPriv().hashCode());
        result = prime * result + ((getReloadPriv() == null) ? 0 : getReloadPriv().hashCode());
        result = prime * result + ((getShutdownPriv() == null) ? 0 : getShutdownPriv().hashCode());
        result = prime * result + ((getProcessPriv() == null) ? 0 : getProcessPriv().hashCode());
        result = prime * result + ((getFilePriv() == null) ? 0 : getFilePriv().hashCode());
        result = prime * result + ((getGrantPriv() == null) ? 0 : getGrantPriv().hashCode());
        result = prime * result + ((getReferencesPriv() == null) ? 0 : getReferencesPriv().hashCode());
        result = prime * result + ((getIndexPriv() == null) ? 0 : getIndexPriv().hashCode());
        result = prime * result + ((getAlterPriv() == null) ? 0 : getAlterPriv().hashCode());
        result = prime * result + ((getShowDbPriv() == null) ? 0 : getShowDbPriv().hashCode());
        result = prime * result + ((getSuperPriv() == null) ? 0 : getSuperPriv().hashCode());
        result = prime * result + ((getCreateTmpTablePriv() == null) ? 0 : getCreateTmpTablePriv().hashCode());
        result = prime * result + ((getLockTablesPriv() == null) ? 0 : getLockTablesPriv().hashCode());
        result = prime * result + ((getExecutePriv() == null) ? 0 : getExecutePriv().hashCode());
        result = prime * result + ((getReplSlavePriv() == null) ? 0 : getReplSlavePriv().hashCode());
        result = prime * result + ((getReplClientPriv() == null) ? 0 : getReplClientPriv().hashCode());
        result = prime * result + ((getCreateViewPriv() == null) ? 0 : getCreateViewPriv().hashCode());
        result = prime * result + ((getShowViewPriv() == null) ? 0 : getShowViewPriv().hashCode());
        result = prime * result + ((getCreateRoutinePriv() == null) ? 0 : getCreateRoutinePriv().hashCode());
        result = prime * result + ((getAlterRoutinePriv() == null) ? 0 : getAlterRoutinePriv().hashCode());
        result = prime * result + ((getCreateUserPriv() == null) ? 0 : getCreateUserPriv().hashCode());
        result = prime * result + ((getEventPriv() == null) ? 0 : getEventPriv().hashCode());
        result = prime * result + ((getTriggerPriv() == null) ? 0 : getTriggerPriv().hashCode());
        result = prime * result + ((getCreateTablespacePriv() == null) ? 0 : getCreateTablespacePriv().hashCode());
        result = prime * result + ((getSslType() == null) ? 0 : getSslType().hashCode());
        result = prime * result + ((getMaxQuestions() == null) ? 0 : getMaxQuestions().hashCode());
        result = prime * result + ((getMaxUpdates() == null) ? 0 : getMaxUpdates().hashCode());
        result = prime * result + ((getMaxConnections() == null) ? 0 : getMaxConnections().hashCode());
        result = prime * result + ((getMaxUserConnections() == null) ? 0 : getMaxUserConnections().hashCode());
        result = prime * result + ((getPlugin() == null) ? 0 : getPlugin().hashCode());
        result = prime * result + ((getPasswordExpired() == null) ? 0 : getPasswordExpired().hashCode());
        result = prime * result + ((getIsRole() == null) ? 0 : getIsRole().hashCode());
        result = prime * result + ((getSslCipher() == null) ? 0 : getSslCipher().hashCode());
        result = prime * result + ((getX509Issuer() == null) ? 0 : getX509Issuer().hashCode());
        result = prime * result + ((getX509Subject() == null) ? 0 : getX509Subject().hashCode());
        result = prime * result + ((getAuthenticationString() == null) ? 0 : getAuthenticationString().hashCode());
        return result;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user
     *
     * @mbggenerated
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", sslCipher=").append(sslCipher);
        sb.append(", x509Issuer=").append(x509Issuer);
        sb.append(", x509Subject=").append(x509Subject);
        sb.append(", authenticationString=").append(authenticationString);
        sb.append("]");
        return sb.toString();
    }
}