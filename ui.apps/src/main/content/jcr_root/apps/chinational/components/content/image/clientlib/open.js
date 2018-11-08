(function ($document, author) {
    var desktop = {
        icon: 'coral-Icon--windows8',
        text: 'Edit Desktop Image',
        handler: function (editable, param, target) {
            console.log('you clicked desktop');
            $("#desktop").show();
        },
        condition: function (editable) {
            //show this action only for below component type
            return editable.type === "chinational/components/content/image";
        },
        isNonMulti: true
    };

    var ipad = {
        icon: 'coral-Icon--apple',
        text: 'Edit Ipad Image',
        handler: function (editable, param, target) {
            console.log('you clicked ipad');
            $("#ipad").show();
        },
        condition: function (editable) {
            //show this action only for below component type
            return editable.type === "chinational/components/content/image";
        },
        isNonMulti: true
    };

    var mobile = {
        icon: 'coral-Icon--mobileServices',
        text: 'Edit Mobile Image',
        handler: function (editable, param, target) {
            console.log('you clicked mobile');
            $("#mobile").show();
        },
        condition: function (editable) {
            //show this action only for below component type
            return editable.type === "chinational/components/content/image";
        },
        isNonMulti: true
    };

    $document.on('cq-layer-activated', function (ev) {
        if (ev.layer === 'Edit') {
            author.EditorFrame.editableToolbar.registerAction('EAEM_OPEN_DIALOG1', desktop);
            author.EditorFrame.editableToolbar.registerAction('EAEM_OPEN_DIALOG2', ipad);
            author.EditorFrame.editableToolbar.registerAction('EAEM_OPEN_DIALOG3', mobile);
        }
    });
})($(document), Granite.author);

