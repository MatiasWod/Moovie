const fs = require('fs');
const path = './build/index.html';
const jspDirective = '<%@ page session="false" %>\n';

const content = fs.readFileSync(path, 'utf8');
fs.writeFileSync(path, jspDirective + content);
