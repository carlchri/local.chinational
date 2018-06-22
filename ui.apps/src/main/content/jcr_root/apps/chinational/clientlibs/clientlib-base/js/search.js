function searchResultSubmit() {
    var srchBoxObj = document.getElementById('srchtext');
    var origText = document.getElementById('origText').value;
    var srchBoxText = srchBoxObj.value;
    if (srchBoxText == origText) {
        //console.log("clear the content");
        srchBoxObj.value='';
        srchBoxObj.focus();
        srchBoxObj.select();
        updateSearchIcon()
        return;
    }
    // orig text not same, hit submit
    document.forms['search-form-embedded'].submit();
}

function updateSearchIcon(){
        //console.log("update search icon");
        // update search icon
        document.getElementById('srchicon').classList.remove('sprite');
        document.getElementById('srchicon').classList.remove('icon-search-x');
        document.getElementById('srchicon').classList.add('sprite-v2');
        document.getElementById('srchicon').classList.add('icon-search');
}

function searchTextChanged() {
    //console.log("searchTextChanged called");
    var hcObj = document.getElementById('origTextChanged');
    var hasChanged = hcObj.value;
    if (hasChanged == 0) {
        updateSearchIcon();
        hasChanged = 1;
    }
}