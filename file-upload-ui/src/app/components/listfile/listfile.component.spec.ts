import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListfileComponent } from './listfile.component';

describe('ListfileComponent', () => {
  let component: ListfileComponent;
  let fixture: ComponentFixture<ListfileComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ListfileComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ListfileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
