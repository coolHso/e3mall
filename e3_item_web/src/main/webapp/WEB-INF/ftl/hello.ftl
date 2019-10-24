<#--${hello}-->
<#--${key}-->
<#--${student.id}-->
<#--${student.name}-->
<#--${student.age}-->
<#--<#list studentList as student>
    <tr>
        <td>${student_index}</td>
        <td>${student.id}</td>
        <td>${student.name}</td>
        <td>${student.age}</td>
    </tr>
</#list>-->
<#--<#list studentList as student>
    <#if student.id='2'>
        <tr>
            <td>${student_index}</td>
            <td>${student.id}</td>
            <td>${student.name}</td>
            <td>${student.age}</td>
        </tr>
    <#else>
       ${student_index}
    </#if>
</#list>-->
<#--当前日期：${date?date}</<br>
当前时间：${date?time}</<br>
当前日期和时间：${date?datetime}
自定义日期格式：${date?string("yyyy/MM/dd HH:mm:ss")}-->

${item!"item为空"}
<br>
<#if item??>
    <p>item不为空</p>
<#else>
    <p>item为空</p>
</#if>
<#include "hello.txt" />
<#include "hello.ftl" />



