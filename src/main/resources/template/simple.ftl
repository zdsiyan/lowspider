<!DOCTYPE html>
<html lang="en">
<head>
<title>${book.title}</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<head>
<body>
<!-- index -->
<h1>目录</h1>
${md.marked(util.generateIndexMD(book))}
<!-- content -->
<h1>正文</h1>
<#list book.chapters as chapter><#lt />
${md.marked("## "+chapter.title)}
${md.marked(chapter.content)}
</#list>
</body>