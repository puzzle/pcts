export interface TableData {
  rows: {
    cells: string[];
  }[];
}


export const degreeTableData: TableData = {
  rows: [{
    cells: ['01.09.2016 - 30.06.2019',
      'Bachelor of Arts in English Language and Literature',
      'PhD']
  }]
};

export const experienceTableData: TableData = {
  rows: [{
    cells: [
      '01.06.2021 - heute',
      'TechNova Solutions Marketing Intern',
      'Assisted in content strategy and social media analytics.',
      'Berufserfahrung'
    ]
  }]
};


export const certificateTableData: TableData = {
  rows: [{
    cells: ['01.11.2022',
      'Cisco CCNA',
      'Lifetime certification.']
  }]
};


export const leadershipExperienceTableData: TableData = {
  rows: [{
    cells: ['Ski Camp Manager',
      'This is quite hard']
  }]
};


export const memberCalculationTableData: TableData = {
  rows: [{
    cells: [
      '0.00',
      'Aktiv',
      'system',
      '02.02.2025'
    ]
  }]
};
