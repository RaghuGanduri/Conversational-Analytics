import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConversationalAnalytics } from './conversational-analytics';

describe('ConversationalAnalytics', () => {
  let component: ConversationalAnalytics;
  let fixture: ComponentFixture<ConversationalAnalytics>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConversationalAnalytics],
    }).compileComponents();

    fixture = TestBed.createComponent(ConversationalAnalytics);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
