export const createExperience = (token: string) => {
  return cy.request({
    method: 'POST',
    url: 'http://localhost:4200/api/v1/experiences',
    headers: {
      Authorization: `Bearer ${token}`
    },
    body: {
      name: 'Software Engineer',
      memberId: 2,
      experienceTypeId: 1,
      comment: 'Worked on backend APIs and DevOps tasks.',
      employer: 'TechNova Solutions',
      percent: 100,
      startDate: '2023-10-10',
      endDate: '2027-12-10'
    }
  });
};

export const createLeadershipExperience = (token: string) => {
  return cy.request({
    method: 'POST',
    url: 'http://localhost:4200/api/v1/leadership-experiences',
    headers: {
      Authorization: `Bearer ${token}`
    },
    body: {
      memberId: 2,
      leadershipExperienceTypeId: 3,
      comment: 'This is a comment'
    }
  });
};

export const createDegree = (token: string) => {
  return cy.request({
    method: 'POST',
    url: 'http://localhost:4200/api/v1/degrees',
    headers: {
      Authorization: `Bearer ${token}`
    },
    body: {
      name: 'Bachelor in mathematics',
      memberId: 2,
      typeId: 1,
      institution: 'GIBB',
      completed: true,
      comment: '',
      startDate: '2000-10-10',
      endDate: '2001-12-10'
    }
  });
};

export const createCertificate = (token: string) => {
  return cy.request({
    method: 'POST',
    url: 'http://localhost:4200/api/v1/certificates',
    headers: {
      Authorization: `Bearer ${token}`
    },
    body: {
      memberId: 2,
      certificateTypeId: 10,
      completedAt: '2000-10-10',
      validUntil: null,
      comment: ''
    }
  });
};

export const createCalculation = (token: string) => {
  return cy.request({
    method: 'POST',
    url: 'http://localhost:4200/api/v1/calculations',
    headers: {
      Authorization: `Bearer ${token}`
    },
    body: {
      memberId: 2,
      state: 'DRAFT',
      roleId: 2,
      certificateCalculations: [{
        id: null,
        certificateId: 2
      }],
      degreeCalculations: [{
        id: null,
        degreeId: 2,
        relevancies: {
          STRONGLY: 100
        },
        comment: ''
      }],
      experienceCalculations: [{
        id: null,
        experienceId: 13,
        comment: null,
        relevancy: 'STRONGLY'
      }],
      leadershipExperienceCalculations: [{
        id: null,
        leadershipExperienceId: 2
      }]
    }
  });
};
