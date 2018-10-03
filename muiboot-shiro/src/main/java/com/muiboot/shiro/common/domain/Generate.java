package com.muiboot.shiro.common.domain;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by 75631 on 2018/10/3.
 */
@Table(name = "M_GENERATE")
public class Generate implements Serializable {
    public final static String CACHE_GENERATE_KEY ="CACHE_GENERATE_KEY";
    @Id
    @Column(name = "CLASS_NAME")
    private String className;
    @Column(name = "CURRENTVAL")
    private long currentval=1;
    @Column(name = "MAXVAL")
    private long maxval=5;
    @Column(name = "INCREMENT")
    private long increment=1;
    @Column(name = "CACHE")
    private long cache=10;
    @Transient
    private AtomicBoolean empty=new AtomicBoolean(false);
    @Transient
    private AtomicLong atomicCurrentval;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public long getCurrentval() {
        return currentval;
    }

    public void setCurrentval(long currentval) {
        this.currentval = currentval;
    }

    public long getMaxval() {
        return maxval;
    }

    public void setMaxval(long maxval) {
        this.maxval = maxval;
    }

    public long getIncrement() {
        return increment;
    }

    public void setIncrement(long increment) {
        this.increment = increment;
    }

    public long getCache() {
        return cache;
    }

    public void setCache(long cache) {
        this.cache = cache;
    }

    public boolean getEmpty() {
        return empty.get();
    }

    public AtomicLong getAtomicCurrentval() {
        if (null==atomicCurrentval){
            atomicCurrentval=new AtomicLong(currentval);
        }
        return atomicCurrentval;
    }

    public void setAtomicCurrentval(AtomicLong atomicCurrentval) {
        this.atomicCurrentval = atomicCurrentval;
    }

    public  Generate  next(){
        Generate next=null;
        try {
             next = (Generate) BeanUtils.cloneBean(this);
        } catch (Exception e) {
            return null;
        }
        next.setCurrentval(next.getAtomicCurrentval().addAndGet(increment));
        if (next.getAtomicCurrentval().get()>=next.maxval){
            next.empty.compareAndSet(false,true);
        }
        return next;
    }

    @Override
    public boolean equals( Object obj )
    {
        Generate generate= (Generate) obj;
        if( this==obj ) return true;
        if (this.currentval==generate.getCurrentval()&&this.maxval==generate.maxval&&this.increment==generate.increment&&this.className.equals(generate.getClassName())){
            return true;
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
