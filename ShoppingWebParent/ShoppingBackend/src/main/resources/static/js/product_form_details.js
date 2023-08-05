$(document).ready(function() {
	$("[id^='linkRemoveDetail']").each(function(index) {
		$(this).click(function() {
			removeDetailSectionByIndex(index);
		});
	});
});

function addNextDetailSection() {
    allDivDetails = $("[id^='divDetail']");
    divDetailsCount = allDivDetails.length;

    htmlDetailSection = `
        <div class="row" id="divDetail${divDetailsCount}">
            <div class="col-6 d-flex align-items-center">
                <input type="hidden" name="detailIDs" value="0" />
                <label class="m-3">Name:</label>
                <input type="text" class="form-control" name="detailNames" maxlength="255" />
            </div>
            <div class="col-5 d-flex align-items-center">
                <label class="m-3">Value:</label>
                <input type="text" class="form-control" name="detailValues" maxlength="255" />
            </div>
            <div class="col-1 d-flex align-items-center justify-content-end">
                <a class="btn fas fa-times-circle fa-2x icon-dark"
                    href="javascript:removeDetailSectionById('divDetail${divDetailsCount}')"
                    title="Remove this detail"></a>
            </div>
        </div>
    `;

    $("#divProductDetails").append(htmlDetailSection);

    $("input[name='detailNames']").last().focus();
}

function removeDetailSectionById(id) {
    $("#" + id).remove();
}

function removeDetailSectionByIndex(index) {
	$("#divDetail" + index).remove();
}
