function searchResultSubmit() {
    var srchBoxObj = document.getElementById('srchtext');
    var origText = document.getElementById('origText').value;
    var srchBoxText = srchBoxObj.value;
    if (srchBoxText == origText) {
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
        // update search icon
        document.getElementById('srchicon').classList.remove('sprite');
        document.getElementById('srchicon').classList.remove('icon-share');
        document.getElementById('srchicon').classList.add('sprite-v2');
        document.getElementById('srchicon').classList.add('icon-search');
}

function searchTextChanged() {
    var hcObj = document.getElementById('origTextChanged');
    var hasChanged = hcObj.value;
    if (hasChanged == 0) {
        updateSearchIcon();
        hasChanged = 1;
    }
}