import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FileUploadService } from 'src/app/services/file-upload.service';

@Component({
  selector: 'app-upload-file',
  templateUrl: './uploadfile.component.html',
  styleUrls: ['./uploadfile.component.css']
})
export class UploadfileComponent implements OnInit {

  fileToUpload: File | null = null;

  constructor(private fileUploadService: FileUploadService) { }

  ngOnInit(): void {
  }

  @ViewChild('fileInput') fileInput!: ElementRef;
  fileAttr = 'Choose File';

  uploadFileToActivity(inputEvent: any) {
    const file: File = inputEvent.target.files.item(0);
    if (file) {
           
      this.fileUploadService
        .uploadFile(file)
        .subscribe(
          data => {
            alert("file uploaded successfully.");
          },
          error => {
            console.log("Error occurred while uploading file.");
          }
        );
    }
  }

}
