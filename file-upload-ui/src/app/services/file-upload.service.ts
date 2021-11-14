import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { catchError } from 'rxjs/operators';
import { of } from 'rxjs/internal/observable/of';
import { FileInfo } from '../model/fileInfo';

@Injectable({
  providedIn: 'root'
})
export class FileUploadService {

  constructor(private httpClient: HttpClient) { }

  baseURL: string = "http://localhost:8080/";

  /**
   * List all the files from the server.
   * 
   * @returns lis of file info containing details of the file.
  */
  listUploadedFiles(): Observable<FileInfo[]> {
    return this.httpClient
      .get<FileInfo[]>(this.baseURL)
      .pipe(
        catchError(this.handleError<FileInfo[]>('list all files', []))
      );

  }

  /**
     * Get file from the server.
     *
     * @param fileName name of the file.
     * @returns file if present.
     */
  getFile(fileName: string): Observable<string> {
    const url = this.baseURL + "/files/" + fileName;
    return this.httpClient
      .get<string>(url)
      .pipe(
        catchError(this.handleError<string>('get a file'))
      );
  }

  /**
   * Upload the file on the server.
   * 
   * @param fileToUpload file to be uploaded.
   * @returns true if file is uploaded successfully else false.
  */
  uploadFile(fileToUpload: File): Observable<boolean> {
    const formData: FormData = new FormData();
    formData.append('file', fileToUpload);
    return this.httpClient
      .post<boolean>(this.baseURL, formData)
      .pipe(
        catchError(this.handleError<boolean>('upload a file'))
      );
  }

  /**
     * Delete all the files from the location.
     *
     * @returns List of file path which are deleted.
     */
  deleteAllFiles(): Observable<string[]> {
    return this.httpClient
      .delete<string[]>(this.baseURL)
      .pipe(
        catchError(this.handleError<string[]>('delete all file'))
      );
  }

  /**
     * Delete a file from the server.
     *
     * @param fileName name of the file to be deleted.
     * @returns list of files
     */
  deleteFile(fileName: string): Observable<string> {
    const url = this.baseURL + "/files/" + fileName;
    return this.httpClient
    .delete<string>(url)
    .pipe(
      catchError(this.handleError<string>('delete file'))
    );
  }

  /**
 * Handle Http operation that failed. Let the app continue.
 * 
 * @param operation - name of the operation that failed
 * @param result - optional value to return as the observable result
 * 
 */
  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }
}
