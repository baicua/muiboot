$(function() {
    $("#apply-print").on("shown.bs.modal",function () {
    });
});
// If absolute URL from the remote server is provided, configure the CORS
// header on that server.
var url = ctx+'file/filetest.pdf';
// The workerSrc property shall be specified.
PDFJS.workerSrc = ctx+'js/pdf/pdf.worker.js';
// Asynchronous download of PDF
var loadingTask = PDFJS.getDocument(url);
loadingTask.promise.then(function(pdf) {
    console.log('PDF loaded');

    // Fetch the first page
    var pageNumber = 1;
    pdf.getPage(pageNumber).then(function(page) {
        console.log('Page loaded');

        var scale = 2.0;
        var viewport = page.getViewport(scale);

        // Prepare canvas using PDF page dimensions
        var canvas = document.getElementById('pdf-content');
        var context = canvas.getContext('2d');
        canvas.height = viewport.height;
        canvas.width = viewport.width;
        // Render PDF page into canvas context
        var renderContext = {
            canvasContext: context,
            viewport: viewport
        };
        var renderTask = page.render(renderContext);
        renderTask.then(function () {
            context.scale(0.5,0.5);
            context.stroke();
            console.log('Page rendered');
        });
    });
}, function (reason) {
    // PDF loading error
    console.error(reason);
});