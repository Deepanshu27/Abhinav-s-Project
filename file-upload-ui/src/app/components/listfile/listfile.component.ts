import { Component, OnInit } from '@angular/core';
import { FileInfo } from 'src/app/model/fileInfo';
import { FileUploadService } from 'src/app/services/file-upload.service';

@Component({
  selector: 'app-list-file',
  templateUrl: './listfile.component.html',
  styleUrls: ['./listfile.component.css']
})
export class ListfileComponent implements OnInit {

  constructor(private fileUploadService: FileUploadService) { }

  fileList: FileInfo[] = [];

  ngOnInit(): void {
    this.listUploadedFiles();
  }

  displayedColumns: string[] = ['position', 'fileName', 'createdDate'];

  listUploadedFiles() {
    this.fileUploadService
    .listUploadedFiles()
    .subscribe(
      data => {
        this.fileList = data;
      },
      error => {
        alert("Error occurred while fetching uploaded files list.");
      }
    )
  }
}
