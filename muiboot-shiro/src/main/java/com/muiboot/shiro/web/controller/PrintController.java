package com.muiboot.shiro.web.controller;

import com.muiboot.shiro.common.controller.BaseController;
import com.muiboot.shiro.common.domain.ResponseBo;
import com.muiboot.shiro.common.util.HttpUtils;
import com.muiboot.shiro.common.util.UrlUtils;
import com.muiboot.shiro.web.vo.Printer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.standard.PrinterState;
import java.awt.print.PrinterJob;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title: 打印机</p>
 * <p>Description: </p>
 * <p>Company: www.lvmama.com</p>
 *
 * @author jin
 * @version 1.0 2018/7/30
 */
@Controller
public class PrintController  extends BaseController {

    /**
    * <p>Description: 获取打印机列表</p>
    * @version 1.0 2018/7/30
    * @author jin
    */
    @RequestMapping("print/getPrinters")
    @ResponseBody
    public ResponseBo getPrinters() {
        try {
            PrintService printService[]=PrinterJob.lookupPrintServices();
            if (null==printService||printService.length==0)
                return ResponseBo.error("没有找到可用打印机！");
            List<Printer> printers = new ArrayList<>();
            //定位默认的打印服务
            PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();
            for (int i=0;i<printService.length;i++){
                Printer printer = new Printer(printService[i].getName());
                if (defaultService!=null&&defaultService.equals(printService[i])){
                    printer.setDefault(true);
                }
                printer.setAttributes(printService[i]);
                printers.add(printer);
            }
            return ResponseBo.ok(printers);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBo.error("打印机读取失败！");
        }
    }
}
