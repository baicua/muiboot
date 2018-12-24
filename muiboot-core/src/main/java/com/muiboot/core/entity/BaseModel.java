package com.muiboot.core.entity;
import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import javax.persistence.Transient;

/**
 * <p>Description: 实体基类</p>
 *
 * @version 1.0 2018/9/18
 */
public abstract class BaseModel implements Serializable{

    /**
     * The Generated SerialVersionUID
     */
    private static final long serialVersionUID = 7766184319541530720L;

    /**
     * 实体主键
     */
    @Transient
    public abstract Object getPrimaryKey();
    /**
     * Common implement equals method
     */
    @Override
    public boolean equals( Object obj )
    {
        if( this==obj ) return true;

        if( !( obj instanceof BaseModel ) )
            return false;

        BaseModel target = (BaseModel)obj;

        if( this.getPrimaryKey()!=null)
        {
            return this.getPrimaryKey().equals( target.getPrimaryKey() );
        }

        if( target.getPrimaryKey()!=null)
        {
            return false;
        }

        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     * Generate the hash code
     */
    @Override
    public int hashCode()
    {
        if( this.getPrimaryKey()!=null)
        {
            return this.getPrimaryKey().hashCode();
        }

        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * Common implement toString method
     */
    @Override
    public String toString()
    {
        return ReflectionToStringBuilder.toString( this );
    }
}
