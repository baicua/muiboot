$(function() {
    $("#apply-print").on("shown.bs.modal",function () {
/*        var attId = $("#apply-print").find("input[name='attId']").val();
        $('#pdf-content').empty();
        var ajaxUrl = ctx+"getAtt/"+attId;
        $.ajax({type: "get",url: ajaxUrl,dataType: "json",
            success: function(r) {
                if (r.code == 0) {
                   showPdf(r.msg);
                } else {
                    $MB.n_warning(r.msg);
                }
            }
        });*/
    });
});
function showPdf(data) {
    // If absolute URL from the remote server is provided, configure the CORS
// header on that server.
// The workerSrc property shall be specified.
/*    PDFJS.workerSrc = ctx+'js/pdf/pdf.worker.min.js';
    PDFJS.cMapUrl = ctx+'js/pdf/cmaps/';
    PDFJS.cMapPacked = true;*/
// Asynchronous download of PDF
    var pdfdata = converData(data);
    //pdfjsLib.PDFWorker=ctx+'js/pdf/pdf.min.js';
    PDFJS.cMapUrl = ctx+'js/pdf/cmaps/';
    pdfjsLib.GlobalWorkerOptions.workerSrc = ctx+'js/pdf/pdf.worker.min.js';
    //pdfjsLib.GlobalWorkerOptions.cMapUrl = ctx+'js/pdf/cmaps/';
    //pdfjsLib.GlobalWorkerOptions.cMapPacked =  true;
    //pdfjsLib.PDFWorker.cMapUrl = ctx+'js/pdf/cmaps/';
    //pdfjsLib.cMapPacked = true;
    pdfjsLib.getDocument(pdfdata).then(function(pdf) {
        var $pop = $('#pdf-content');
        var shownPageCount = pdf.numPages < 50 ? pdf.numPages : 50;//设置显示的编码
        var getPageAndRender = function (pageNumber) {
/*            var extractTextCapability = (0, PDFJS.createPromiseCapability)();
            _this2.extractTextPromises[pageNumber] = extractTextCapability.promise;*/
            pdf.getPage(pageNumber).then(function (page) {
/*                var content=page.getTextContent({ normalizeWhitespace: true });
                var textItems = content.items;
                var strBuf = [];
                for (var j = 0, jj = textItems.length; j < jj; j++) {
                    strBuf.push(textItems[j].str);
                }
                _this2.pageContents[pageNumber] = strBuf.join('');
                extractTextCapability.resolve(pageNumber);*/
                var scale = 1;
                var viewport = page.getViewport(scale);
                var $canvas = $('<canvas>该浏览器不支持预览，请升级浏览器。</canvas>').attr({
                    'height': viewport.height,
                    'width': viewport.width
                });
                $pop.append($canvas);

                page.render({
                    canvasContext: $canvas[0].getContext('2d'),
                    viewport: viewport
                });
            });
            if (pageNumber < shownPageCount) {
                pageNumber++;
                getPageAndRender(pageNumber);
            }
        };
        getPageAndRender(1);
    });
}
function convertDataURIToBinary(dataURI) { //将encodeBase64解码
    var raw = window.atob(dataURI);
    var rawLength = raw.length;
    //转换成pdf.js能直接解析的Uint8Array类型,见pdf.js-4068
    var array = new Uint8Array(new ArrayBuffer(rawLength));
    for(i = 0; i < rawLength; i++) {
        array[i] = raw.charCodeAt(i);
    }
    return array;
}
/*将请求来的base64编码的pdf文件，替换多余的空格和换行（为了兼容其他浏览器）
 * 再使用浏览器自带的atob()的方式解析
 * */
/*转化编码格式*/
function converData(data) {
    return "data:application/pdf;base64,"+data;
}