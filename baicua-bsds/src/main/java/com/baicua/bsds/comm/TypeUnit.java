package com.baicua.bsds.comm;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author jin
 * @version 1.0 2018/7/16
 */
public enum TypeUnit {
    SHEET{
        public SheetType flow(){return SheetType.FLOW;}
        public SheetType standard(){return SheetType.STANDARD;}
    },
    BOOK{
        public int applyType(){return 3;}
    };
    enum SheetType{
        FLOW{
            public int applyType(){return 1;}
        },
        STANDARD{
            public int applyType(){return 2;}
        };
        public int applyType(){
            throw new AbstractMethodError();
        }
    }
    public SheetType flow() {
        throw new AbstractMethodError();
    }
    public SheetType standard() {
        throw new AbstractMethodError();
    }
    public int applyType(){
        throw new AbstractMethodError();
    }
}
