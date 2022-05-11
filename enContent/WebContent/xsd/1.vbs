Set objFso = CreateObject("Scripting.FileSystemObject")
Set Folder = objFSO.GetFolder("c:\test\")

For Each File In Folder.Files
    sNewFile = File.Name
    sNewFile = Replace(sNewFile,"swift.rma$","")
    if (sNewFile<>File.Name) then 
        File.Move(File.ParentFolder+"\"+sNewFile)
    end if

Next